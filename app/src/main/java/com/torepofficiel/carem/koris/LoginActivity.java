package com.torepofficiel.carem.koris;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.DatabaseAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Abonnement;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.DialogActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.CryptageSha512;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends DialogActivity {


    private static WeakReference<LoginActivity> myLoginActivity;

    // UI references.
    private TextView mPurseId;
    private EditText mPasswordView;
    private RelativeLayout mProgressView;
    private ProgressBar mProgressBar;
    private View mLoginFormView;
    private Button mSignInButton;
    private TextView mPasswordLabel,
            mPasswordView_error;
    private Button mMdpOublie;
    private ImageView mLogo;

    private static boolean isPassword_empty = true;
    private long mBackPressed;
    private Toast exit_toast;

    private ImageView mImg_clear_mdp;
    private MediaPlayer media;


    public boolean showingLoginProgress,
            showingRegisterProgress,
            controll;

    private RelativeLayout mLogin_main_layout;

    public static String nom_prenom_utilisateur ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myLoginActivity = new WeakReference<>(this);
        ErrorActivity.currentActivity = new WeakReference<>((Activity) this);

        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = DatabaseAsyncTask.selectionnerUtilisateur(1L);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        nom_prenom_utilisateur = utilisateur.getNom() + " " + utilisateur.getPrenom();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/andrfont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        initialiseUI();

        Intent intent = getIntent();
        if(intent.hasExtra(RegistrationActivity.CONNECTE)){
            showAlertDialog(-1, getString(R.string.title_register_success),
                    getString(R.string.msg_register_success),
                    R.mipmap.logo_small, getString(R.string.ok_dialog_cnx_checking), "");
        }

        if(intent.hasExtra(BienvenueActivity.COMPTE_CHARGE)){
            showAlertDialog(-1, getString(R.string.title_compte_charge),
                    getString(R.string.msg_compte_charge),
                    R.mipmap.logo_small, getString(R.string.ok_dialog_cnx_checking), "");
        }

        /*Animation animation = new TranslateAnimation((-((float)mLogin_main_layout.getWidth())), 0, 0, 0 );
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateInterpolator());
        //animation.setFillAfter(true);
        //animation.setFillEnabled(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLogin_main_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //mLogin_main_layout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        mLogin_main_layout.startAnimation(animation);*/

        /* FOR EMULATOR TESTING PURPOSE ONLY /

        final String contenu = LoginActivity.getInstance().getString(R.string.no_file, "4546435",
                "4544235", "rester");

                setmNoFile(contenu);


        // End EMULATOR TESTING PURPOSE */

    }

    /*public void setmNoFile(String content) {

        if(mNoFile.getVisibility() == View.VISIBLE)
            mNoFile.setText(content);
    }*/

    public void setmPurseId(String content) {

        if(mPurseId.getVisibility() == View.VISIBLE)
            mPurseId.setText(content);
    }


    private void initialiseUI() {
        // Set up the login form.

        mLogin_main_layout = (RelativeLayout) findViewById(R.id.login_main_layout);
        mPurseId = (TextView) findViewById(R.id.korisId);

        mPasswordLabel = (TextView) findViewById(R.id.password_label);
        mLogo = (ImageView) findViewById(R.id.logo);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.addTextChangedListener(getPasswordTextWatcher());

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return id == R.id.login || id == EditorInfo.IME_NULL;
            }
        });

        mPasswordView_error = (TextView) findViewById(R.id.mdp_error);

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ClasseUtilitaire.checkAutotime()) {
                        showLoginProgress(true);

                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Utilisateur utilisateur;
                                Abonnement abonnement;
                                try {

                                    utilisateur = DatabaseAsyncTask.selectionnerUtilisateur(1L);
                                    abonnement = DatabaseAsyncTask.selectionnerAbonnement(1L);

                                    if(!abonnement.isEst_null() && !abonnement.isEst_actif()) {
                                        Utilisateur utilisateur_in_file = ClasseUtilitaire.readAnObjectInFile();
                                        if(utilisateur_in_file.getEst_abonne()){
                                            DatabaseAsyncTask.addUtilisateur(utilisateur_in_file);
                                            DatabaseAsyncTask.addAbonnement(utilisateur_in_file.getHisAbonnement());
                                        }
                                    }

                                    if(!abonnement.isEst_null() && abonnement.isEst_actif()) {
                                        if (abonnement.getDate_fin_abonnement().getTime() < System.currentTimeMillis()) {
                                            ContentValues values = new ContentValues();
                                            values.put(DataBaseHandler.EST_ACTIF, false);
                                            DatabaseAsyncTask.modifierAbonnement(values);

                                            values = new ContentValues();
                                            values.put(DataBaseHandler.EST_ABONNE, false);
                                            DatabaseAsyncTask.updateUtilisateur(values);
                                        }
                                    }

                                    if(utilisateur.isEst_null()){

                                        showLoginProgress(false);

                                        showAlertDialog(-1, getString(R.string.titre_connexion_interrompue),
                                                getString(R.string.msg_connexion_impossible),
                                                R.mipmap.logo_small, LoginActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), "");

                                    } else{

                                        String mdp_crypte = CryptageSha512.crypt(mPasswordView.getText().toString().trim());

                                        if(!mdp_crypte.equals(utilisateur.getMot_de_passe())){
                                            showLoginProgress(false);
                                            mPasswordView_error.setVisibility(View.VISIBLE);
                                            mPasswordView_error.setText(getString(R.string.mdp_incorrecte));

                                        } else{
                                            //Connexion
                                            new android.os.Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    media = MediaPlayer.create(LoginActivity.this.getApplicationContext(),R.raw.glass);
                                                    media.start();
                                                }
                                            });

                                            ClasseUtilitaire.showToast(LoginActivity.this, "Connecté", -1);
                                            Intent intent = new Intent(LoginActivity.this, KorisActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            LoginActivity.this.startActivity(intent);
                                            LoginActivity.this.finish();
                                        }
                                    }
                                    utilisateur.release();
                                    utilisateur = null;
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                    showAlertDialog(-1, getString(R.string.titre_dialog_cnx_checking),
                                            getString(R.string.msg_dialog_cnx_checking),
                                            R.mipmap.logo_small, LoginActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), "");
                                }

                            }
                        }, 1000);
                }

                /* FOR EMULATOR TESTING PURPOSE ONLY /

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();

                // End EMULATOR TESTING PURPOSE */

            }
        });

        mImg_clear_mdp = (ImageView) findViewById(R.id.mdp_img_clear);
        mImg_clear_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mPasswordView.getText().toString().isEmpty())
                    mPasswordView.getText().clear();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (RelativeLayout) findViewById(R.id.login_progress);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        mMdpOublie = (Button) findViewById(R.id.mdp_oublie);

    }

    public static LoginActivity getInstance() {
        return (myLoginActivity != null) ? myLoginActivity.get() : null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Calendar now = Calendar.getInstance();
        int d = now.get(Calendar.AM_PM);
        String salutation = (d == 0) ? "Bonjour " : "Bonsoir ";
        mPurseId.setText(getString(R.string.salutation_utilisateur, salutation, nom_prenom_utilisateur));
        now = null;

        /*if (!controll && !main_file_exist && !account_in_local_database)
            registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));*/
            /*showLoginProgress(showingLoginProgress);
            showRegisterProgress(showingRegisterProgress);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (!controll && !main_file_exist && !account_in_local_database)
            unregisterReceiver(receiver);*/
    }


    private TextWatcher getPasswordTextWatcher() {

        return (new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                isPassword_empty = ClasseUtilitaire.isEmptyAndBlank(charSequence.toString());

                if (!isPassword_empty) {
                    mSignInButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                    mSignInButton.setEnabled(true);
                    mImg_clear_mdp.setVisibility(View.VISIBLE);
                } else {
                    mSignInButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_darker_gray));
                    mSignInButton.setEnabled(false);
                    mPasswordView_error.setText("");
                    mImg_clear_mdp.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /*private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mPurseId, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, 0 *//* The request code REQUEST_READ_CONTACTS*//*);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, 0 *//* The request code REQUEST_READ_CONTACTS*//*);
        }
        return false;
    }*/


    /**
     * Shows the progress UI and hides the login form.
     */
    public boolean showLoginProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mSignInButton.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mLogo.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLogo.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        return show;
    }

   /* @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getApplicationContext(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //mEmailView.setAdapter(adapter);
    }
*/

    @Override
    public void doPositiveClick(DialogInterface dialogInterface, int id) {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void doNegativeClick() {

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
    protected void onDestroy() {
        if(media != null)
            media.release();
        super.onDestroy();
    }
}

