package com.torepofficiel.carem.koris;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.ConnexionCheckingAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.DialogActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.MyAlertDialog;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.receivers.NotifiyCnxStateReiceiver;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationActivity extends DialogActivity {

    public static final String CONNECTE = "connecte";

    private FloatingActionButton fab_valide,
            fab_disable;

    private CoordinatorLayout fab_layout;
    private PlaceholderFragment placeholderFragment;

    private long mBackPressed;
    private Toast exit_toast;

    public static String annee_naissance_text;

    private static WeakReference<RegistrationActivity> mActivity;

    private RelativeLayout registration_fragment_container;
    private RelativeLayout registering_progressbar;
    private ProgressBar mProgressBar;

    private NotifiyCnxStateReiceiver receiver;

    private static int naissance_annee;

    public boolean showingProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        mActivity = new WeakReference<>(this);
        ErrorActivity.currentActivity = new WeakReference<>((Activity) this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_back_button);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/andrfont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        receiver = new NotifiyCnxStateReiceiver();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        placeholderFragment = new PlaceholderFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.registration_fragment_container, placeholderFragment);
        ft.commit();

        registration_fragment_container = (RelativeLayout) findViewById(R.id.registration_fragment_container);

        registering_progressbar = (RelativeLayout) findViewById(R.id.registering_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        fab_valide = (FloatingActionButton) findViewById(R.id.fab_valide);
        fab_valide.setEnabled(false);
        fab_valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, placeholderFragment.getNewUtilisateur().toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                /*showAlertDialog(-1, "Nouvel Utilisateur",
                        placeholderFragment.getNewUtilisateur().toString(),
                        R.mipmap.logo_small, RegistrationActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), "")
                .setCancelable(false);*/

                showingProgressbar = showRegisterProgress(true);
                new ConnexionCheckingAsyncTask(2).execute((Void) null);

            }
        });

        fab_disable = (FloatingActionButton) findViewById(R.id.fab_disable);

        fab_layout = (CoordinatorLayout) findViewById(R.id.fab_layout);

    }

    public boolean showRegisterProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        registration_fragment_container.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registration_fragment_container.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        fab_layout.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab_layout.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        registering_progressbar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registering_progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        return show;
    }

    public void updateCustomFbsSectionOne(boolean state) {

        if (state) {
            fab_disable.setVisibility(View.VISIBLE);
            fab_valide.setVisibility(View.INVISIBLE);

        } else {
            fab_valide.setEnabled(true);
            fab_valide.setVisibility(View.VISIBLE);
            fab_disable.setVisibility(View.INVISIBLE);
        }
    }

    public void startAnimationOnFab_layout(Animation animation) {
        fab_layout.startAnimation(animation);
    }

    public static RegistrationActivity getInstance() {
        return (mActivity != null) ? mActivity.get() : null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void checkingConnexionState(boolean state) {
        placeholderFragment.checkingConnexionState(state);
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
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public PlaceholderFragment getPlaceholderFragment() {
        return placeholderFragment;
    }

    @Override
    public void doPositiveClick(DialogInterface dialog, int id) {

        switch (MyAlertDialog.choix) {
            case 2:
                        MyAlertDialog.choix = 3;
                        showAlertDialog(R.layout.annee_naissance, R.string.titre_annee_naisance,
                                -1,
                                R.mipmap.logo_small, R.string.ok_dialog_cnx_checking, -1).setCancelable(false);


                break;

            case 3:

                if(PlaceholderFragment.getAlert() != null)
                    PlaceholderFragment.getAlert().dismissAllowingStateLoss();

                final Calendar myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        placeholderFragment.updateLabel(myCalendar);

                    }
                };

                try {
                    naissance_annee = Integer.parseInt(annee_naissance_text);
                } catch (NumberFormatException e) {
                    naissance_annee = 1900;
                }
                myCalendar.set(naissance_annee, 0, 1);

                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar
                        .get(Calendar.DAY_OF_MONTH)).show();

                break;
        }

    }

    @Override
    public void doNegativeClick() {

    }
}
