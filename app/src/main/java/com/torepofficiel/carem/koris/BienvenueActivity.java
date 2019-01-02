package com.torepofficiel.carem.koris;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.CheckingConnectionAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.DialogActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BienvenueActivity extends DialogActivity {

    public static final String COMPTE_CHARGE = "compte_charge";
    private static WeakReference<BienvenueActivity> bienvenueActivity;

    private long mBackPressed;
    private Toast exit_toast;

    private TextView mBienvenue_msg;
    private Button mRegister_button;
    private RelativeLayout mRegister_progress;
    private ProgressBar mProgressBar;
    private ImageView mLogo;
    public boolean showingRegisterProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenue);

        ErrorActivity.currentActivity = new WeakReference<>((Activity) this);
        bienvenueActivity = new WeakReference<>(this);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/andrfont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        initialise_views();

    }

    private void initialise_views(){
        mLogo = (ImageView) findViewById(R.id.logo);
        mBienvenue_msg = (TextView) findViewById(R.id.bienvenue_message);
        mRegister_progress = (RelativeLayout) findViewById(R.id.register_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        mRegister_button = (Button) findViewById(R.id.register_button);
        mRegister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ClasseUtilitaire.checkAutotime()) {

                    if (ClasseUtilitaire.isEmptyAndBlank(ClasseUtilitaire.getUserSubscriberId())) {
                        Intent intent1 = new Intent(getApplicationContext(), ErrorActivity.class);
                        intent1.putExtra(MainActivity.APP_FREEZED, getString(R.string.app_freezed));
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent1);
                        finish();

                    } else {

                        showingRegisterProgress = showRegisterProgress(true);
                        new CheckingConnectionAsyncTask(true).execute((Void) null);

                    }
                }
            }
        });
    }

    public static BienvenueActivity getInstance(){
        return (bienvenueActivity != null) ? bienvenueActivity.get() : null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public boolean showRegisterProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mBienvenue_msg.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBienvenue_msg.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mRegister_button.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegister_button.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mLogo.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLogo.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mRegister_progress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegister_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        return show;
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + ClasseUtilitaire.TIME_INTERVAL > System.currentTimeMillis()) {
            exit_toast.cancel();
            finish();
            return;
        }
        mBackPressed = System.currentTimeMillis();
        exit_toast = Toast.makeText(this, getString(R.string.exit_toast_msg), Toast.LENGTH_LONG);
        exit_toast.show();
    }

    @Override
    public void doPositiveClick(DialogInterface dialog, int id) {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void doNegativeClick() {

    }
}
