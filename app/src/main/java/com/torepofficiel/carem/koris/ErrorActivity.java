package com.torepofficiel.carem.koris;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.listeners.PhoneState_Listener;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.services.PhoneStateService;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ErrorActivity extends AppCompatActivity {

    TextView error_message;

    public static WeakReference<Activity> currentActivity;

    private long mBackPressed;
    private Toast exit_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/andrfont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        error_message = (TextView) findViewById(R.id.error_message);

        Intent intent = getIntent();

        if(intent.hasExtra(MainActivity.APP_FREEZED)){
            error_message.setText(intent.getStringExtra(MainActivity.APP_FREEZED));
        }

        if(intent.hasExtra(MainActivity.SIM_CHANGEMENT)){
            error_message.setText(intent.getStringExtra(MainActivity.SIM_CHANGEMENT));
        }

        if(intent.hasExtra(PhoneState_Listener.SIM_STATE)){
            error_message.setText(intent.getStringExtra(PhoneState_Listener.SIM_STATE));
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneState_Listener(ErrorActivity.this.getApplicationContext(), false),
                PhoneStateListener.LISTEN_SERVICE_STATE);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + ClasseUtilitaire.TIME_INTERVAL > System.currentTimeMillis()) {
            exit_toast.cancel();
            if (currentActivity != null)
                if (currentActivity.get() != null)
                    currentActivity.get().finish();

            if(PhoneStateService.getInstance() != null)
                PhoneStateService.getInstance().dismisHandler();
            finish();
            return;
        }
        mBackPressed = System.currentTimeMillis();
        exit_toast = Toast.makeText(this, getString(R.string.exit_toast_msg), Toast.LENGTH_LONG);
        exit_toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(PhoneStateService.getInstance() != null)
            PhoneStateService.getInstance().dismisHandler();
    }
}
