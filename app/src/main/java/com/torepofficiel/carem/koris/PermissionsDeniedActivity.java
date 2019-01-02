package com.torepofficiel.carem.koris;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PermissionsDeniedActivity extends AppCompatActivity {

    private long mBackPressed;
    private Toast exit_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_denied);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/andrfont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ErrorActivity.currentActivity = new WeakReference<>((Activity) this);

        TextView permDenied = findViewById(R.id.permDenied);
        String perms_denied = "";
        for(String perm : MainActivity.permissions_denied) {
            if(!perm.isEmpty())
                perms_denied += "- " + perm + "\n";
        }

        permDenied.setText(getString(R.string.permDenied, perms_denied));



    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.permissions_denied = new ArrayList<>();
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

    public void requestPermissions(View v){
        Intent intent = new Intent(PermissionsDeniedActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
        finish();
    }

}
