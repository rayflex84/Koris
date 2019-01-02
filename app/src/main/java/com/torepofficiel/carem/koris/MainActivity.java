package com.torepofficiel.carem.koris;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.DatabaseAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Abonnement;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Code_Securite_Transactions;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Parametres;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.DialogActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.services.PhoneStateService;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION_CODES.M;

enum grantingResultsCode{
   none("none"), accepted("accepted"), refused("refused");

    String name;
    grantingResultsCode(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class MainActivity extends DialogActivity {

    private HashMap<String, Boolean> permissions_control = new HashMap<>();
    public static ArrayList<String> permissions_denied = new ArrayList<>();
    private grantingResultsCode grantedResults = grantingResultsCode.none;
    private boolean cancelTask,
            main_file_exist,
            account_in_local_database;

    public static final String  ROOT_DIRECTORY = "/sdcard/.torep/.officiel/.carem/.koris";
    public static final String UTILISATEUR_FILE = ".fe44345tyg5";
    public static final String NO_MEDIA = ".nomedia";
    public static final String APP_FREEZED= "app_freezed";
    public static final String SIM_CHANGEMENT= "sim_changement";

    private ImageView mLogo;

    AsyncTask<Void, Void, Void > task;

    private WeakReference<Utilisateur> mUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/andrfont.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ErrorActivity.currentActivity = new WeakReference<>((Activity) this);
        mLogo = findViewById(R.id.logo);
        mUtilisateur = new WeakReference<>(new Utilisateur());

        /*Intent intent = new Intent(this, TestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();*/

        /*try {
            if (Build.VERSION.SDK_INT < 17) {
                if (Settings.System.getInt(getContentResolver(), Settings.Global.AUTO_TIME) < 1 ||
                        Settings.System.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE) < 1) {

                    Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                    startActivity(intent);

                }
            } else {
                if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) < 1 ||
                        Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME_ZONE) < 1) {

                    Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                    startActivity(intent);

                }
            }

        } catch(Settings.SettingNotFoundException e){
            e.printStackTrace();
        }*/
        /*Intent intent = getIntent();
        if(intent != null && intent.hasExtra(PermissionsDeniedActivity.REQUEST)){
            executeAplication();
        } else {*/

            executeAplication();
        //}

    }


    private void executeAplication() {

        Intent intent = new Intent(getApplicationContext(), PhoneStateService.class);
        startService(intent);

        Parametres parametres = new Parametres();
        try {
            parametres = DatabaseAsyncTask.selectionnerParametres();
            mUtilisateur = new WeakReference<>(DatabaseAsyncTask.selectionnerUtilisateur(1L));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(parametres.isApp_freezed()){

            try {
                mUtilisateur.get().setHisAbonnement( DatabaseAsyncTask.getCurrentAbonnementsOfAnUtilisateur(
                        1L
                ));

                mUtilisateur.get().setListeCode( DatabaseAsyncTask.selectionnerTousCodes(1L )
                );

                mUtilisateur.get().setApp_freezed(true);

                ClasseUtilitaire.writeAnObjectInFile(mUtilisateur.get());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


            Intent intent2 = new Intent(getApplicationContext(), ErrorActivity.class);
            intent2.putExtra(APP_FREEZED, getString(R.string.app_freezed));
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);
            finish();

        } else {

            if (Build.VERSION.SDK_INT >= M) {
                if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    cancelTask = true;

                } else {
                    cancelTask = false;
                    task = new RequestPermissionsTask(MainActivity.this);
                    task.execute();
                }
            } else {
                cancelTask = true;
            }
            if (cancelTask)
                creeFichiersNecessairesLancerIntent(MainActivity.this);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(getApplicationContext(), PhoneStateService.class);
        startService(intent);
    }

    private boolean mayRequestPermissions() {
        if (Build.VERSION.SDK_INT < M) {
            grantedResults = grantingResultsCode.accepted;
            return true;
        }

        boolean result;
        ArrayList<String> permissions_not_get = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantedResults = grantingResultsCode.none;
            result = (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED);
            permissions_control.put(CAMERA, result);

            result = (checkSelfPermission(RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED);
            permissions_control.put(RECEIVE_SMS, result);

            result = (checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED);
            permissions_control.put(READ_SMS, result);

            result = (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            permissions_control.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, result);

            result = (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            permissions_control.put(WRITE_EXTERNAL_STORAGE, result);

            result = (checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
            permissions_control.put(READ_PHONE_STATE, result);


            for (HashMap.Entry<String, Boolean> permission : permissions_control.entrySet()) {
                if (!permission.getValue())
                    permissions_not_get.add(permission.getKey());
            }

            if (permissions_not_get.size() < 1) {
                return true;
            }

            requestPermissions(permissions_not_get.toArray(new String[]{}), 0); // The request code REQUEST_READ_CONTACTS
        }

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 0 /* The request code REQUEST_READ_CONTACTS*/) {
            //if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            grantedResults = analyseGrantResults(grantResults, permissions);
            perfomWork(this);
        }
    }

    public String treatResultingData(String permissions){
        if(permissions.contains("SMS"))
            if(!permissions_denied.contains("SMS"))
                return "SMS";

        if(permissions.contains("CAMERA"))
            return "Appareil photo";

        if(permissions.contains("READ_EXTERNAL") || permissions.contains("WRITE_EXTERNAL")) {
            if(!permissions_denied.contains("Stockage"))
                return "Stockage";
        }

        if(permissions.contains("PHONE"))
            return "Telephone";

        return "";
    }

    private grantingResultsCode analyseGrantResults(int[] grantResults, String[] permissions){
        int position = 0;
        for(int res : grantResults){
            if(res != PackageManager.PERMISSION_GRANTED){
                permissions_denied.add(treatResultingData(permissions[position]));
            }
            position++;
        }
        if(permissions_denied.size() > 0) {
            return grantingResultsCode.refused;
        }
        return grantingResultsCode.accepted;
    }

    private void perfomWork(Context context){
        task.cancel(true);
        switch (grantedResults){
            default:

            case refused:
                Intent denyIntent = new Intent(context, PermissionsDeniedActivity.class);
                denyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                denyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                denyIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(denyIntent);
                finish();
                break;
            case accepted:
                cancelTask = true;
                creeFichiersNecessairesLancerIntent(context);
                break;

        }
    }

    public void creeFichiersNecessairesLancerIntent(final Context context){

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Animation animation = new TranslateAnimation(0, 0, 0, -(mLogo.getY() - (float) mLogo.getHeight() / 2) );
                animation.setDuration(1000);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setFillAfter(true);
                animation.setFillEnabled(true);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        /*try {
                            DatabaseAsyncTask.addUtilisateur(MainActivity.this, new Utilisateur("Acakpo","Raymond","ray@riche.com",new Timestamp(System.currentTimeMillis()),
                                    ClasseUtilitaire.getUserSubscriberId(),"93445568","WERTYDFG","amour","Lome","Personnel"));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }*/

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        try {

                            File rootDirectory = new File(ROOT_DIRECTORY);
                            rootDirectory.mkdirs();
                            File nomedia = new File(rootDirectory, NO_MEDIA);
                            if(!nomedia.exists())
                                nomedia.createNewFile();


                            if(mUtilisateur == null || mUtilisateur.get() == null)
                                mUtilisateur = new WeakReference<Utilisateur>(DatabaseAsyncTask.selectionnerUtilisateur(1L));

                            account_in_local_database = !(mUtilisateur.get().isEst_null());

                            File utilisateurFile = new File(rootDirectory, MainActivity.UTILISATEUR_FILE);
                            main_file_exist = utilisateurFile.exists();

                            if (!main_file_exist) {

                                if (account_in_local_database) {

                                    mUtilisateur.get().setHisAbonnement( DatabaseAsyncTask.getCurrentAbonnementsOfAnUtilisateur(
                                            1L
                                    ));

                                    mUtilisateur.get().setListeCode( DatabaseAsyncTask.selectionnerTousCodes(
                                            1L )
                                    );

                                    mUtilisateur.get().setApp_freezed(DatabaseAsyncTask.selectionnerParametres()
                                            .isApp_freezed());

                                    utilisateurFile.setReadOnly();

                                    utilisateurFile.createNewFile();
                                    FileOutputStream ostream = new FileOutputStream(utilisateurFile);
                                    ObjectOutputStream os = new ObjectOutputStream(ostream);
                                    os.writeObject(mUtilisateur.get());
                                    ostream.close();
                                    os.close();

                                }
                            } else {
                                if(!account_in_local_database){

                                    FileInputStream instream = new FileInputStream(utilisateurFile);
                                    ObjectInputStream ins = new ObjectInputStream(instream);
                                    mUtilisateur = new WeakReference<>((Utilisateur) ins.readObject());
                                    instream.close();
                                    ins.close();

                                    Abonnement currentAbonnement = mUtilisateur.get().getHisAbonnement();

                                    DatabaseAsyncTask.addUtilisateur(mUtilisateur.get());

                                    if(currentAbonnement != null && !currentAbonnement.isEst_null())
                                        DatabaseAsyncTask.addAbonnement(currentAbonnement);

                                    ContentValues values = new ContentValues();
                                    values.put(DataBaseHandler.APP_FREEZED, mUtilisateur.get().isApp_freezed() );

                                    DatabaseAsyncTask.modifierParametres(values);

                                    if(mUtilisateur.get().getListeCode() != null && mUtilisateur.get().getListeCode().size() > 0) {
                                        for (Code_Securite_Transactions code : mUtilisateur.get().getListeCode()) {
                                            DatabaseAsyncTask.addCode(code);
                                        }
                                    }

                                    if(mUtilisateur.get().isApp_freezed()){
                                        Intent intent1 = new Intent(getApplicationContext(), ErrorActivity.class);
                                        intent1.putExtra(MainActivity.APP_FREEZED, getString(R.string.app_freezed));
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent1);
                                        finish();
                                        return;
                                    }
                                }
                            }

                           //A transformer en service avec receiver
                            if(!mUtilisateur.get().isEst_null()){

                               if(ClasseUtilitaire.checkSimChange(MainActivity.this)){
                                    finish();
                                    return;
                                }

                            }

                        } catch (InterruptedException | ExecutionException | ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }

                        if(main_file_exist || account_in_local_database){
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(context, BienvenueActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mLogo.startAnimation(animation);

            }
        }, 2000);

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private class RequestPermissionsTask extends AsyncTask<Void, Void, Void >{

        WeakReference<Context> contextWeakReference;

        RequestPermissionsTask(Context context){
            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {

            mayRequestPermissions();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(!cancelTask) {
                while (grantedResults == grantingResultsCode.none) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            task.cancel(true);
            //perfomWork(contextWeakReference.get());
        }
    }

    @Override
    public void doPositiveClick(DialogInterface dialogInterface, int id) {

    }

    @Override
    public void doNegativeClick() {

    }
}
