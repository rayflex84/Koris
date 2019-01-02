package com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.MainActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Abonnement;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Code_Securite_Transactions;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Parametres;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dao.AbonnementDao;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dao.CodesDao;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dao.ParametresDao;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dao.UtilisateurDao;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.RetrieveMyApplicationContext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by macbook on 25/08/17.
 */

public class DatabaseAsyncTask {

    //UTILISATEURS IMPLEMENTATIONS

    /*public static Cursor getAll( ) throws InterruptedException, ExecutionException {
        AsyncTask<Void, Void, Cursor> task = new GetAllAsyncTask().execute();
        return task.get();

    }*/

    public static Long addUtilisateur(Utilisateur utilisateur) throws InterruptedException, ExecutionException{
        long result = -1L;
        try {
            AsyncTask<Utilisateur, Void, Long> task = new AddAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(utilisateur);
            result = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }
        return result;
    }

    public static Utilisateur selectionnerUtilisateur(long id) throws InterruptedException, ExecutionException{
        Utilisateur utilisateur = null;
        try {
            AsyncTask<Long, Void, Utilisateur> task = new SelectAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(id);
            utilisateur = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }
        return utilisateur;
    }

    public static void removeUtilisateur(long id){

        try {
            new RemoveAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

    }

    public static void updateUtilisateur(ContentValues contentNewValues){

        try {
            new UpdateAsyncTask(RetrieveMyApplicationContext.getInstance(), contentNewValues).execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

    }

    private static class AddAsyncTask extends AsyncTask<Utilisateur, Void, Long>{

        private WeakReference<UtilisateurDao> utilisateurDao;
        private WeakReference<Context> mContext;

         AddAsyncTask(Context context ){
             if(context == null){
                 context = ErrorActivity.currentActivity.get();
                 if(context == null){
                     context = RetrieveMyApplicationContext.getAppContext();
                     if(context == null) {
                         context = new Activity();
                     }
                 }
             }
            this.utilisateurDao = new WeakReference<>(
                    new UtilisateurDao(context));
             this.mContext = new WeakReference<>(
                     context);
        }

        @Override
        protected Long doInBackground(Utilisateur... utilisateurs) {
            return utilisateurDao.get().ajouter(utilisateurs[0]);
        }

    }

    private static class SelectAsyncTask extends AsyncTask<Long, Void, Utilisateur>{

        private WeakReference<UtilisateurDao> utilisateurDao;
        private WeakReference<Context> mContext;

        SelectAsyncTask(Context context ){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.utilisateurDao = new WeakReference<>(
                    new UtilisateurDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Utilisateur doInBackground(Long... longs) {
            return utilisateurDao.get().selectionner(longs[0]);
        }

    }

    private static class RemoveAsyncTask extends AsyncTask<Long, Void, Void>{

        private WeakReference<UtilisateurDao> utilisateurDao;
        private WeakReference<Context> mContext;

        RemoveAsyncTask(Context context ){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.utilisateurDao = new WeakReference<>(
                    new UtilisateurDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Void doInBackground(Long... longs) {
            utilisateurDao.get().supprimer(longs[0]);

            return null;
        }

    }

    private static class UpdateAsyncTask extends AsyncTask<Void, Void, Void>{

        private WeakReference<UtilisateurDao> utilisateurDao;
        private WeakReference<ContentValues> contentNewValues;
        private WeakReference<Context> mContext;

        UpdateAsyncTask(Context context,ContentValues contentNewValues){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.utilisateurDao = new WeakReference<>(
                    new UtilisateurDao(context));
            this.contentNewValues = new WeakReference<>(contentNewValues);
            this.mContext = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            utilisateurDao.get().modifier(contentNewValues.get());

            return null;
        }

    }

    /*private static class GetAllAsyncTask extends AsyncTask<Void, Void, Cursor>{

        private WeakReference<UtilisateurDao> utilisateurDao;

        GetAllAsyncTask( ){

            this.utilisateurDao = new WeakReference<UtilisateurDao>(
                    new UtilisateurDao());
        }

        @Override
        protected Cursor doInBackground(Void... voids) {

            return utilisateurDao.get().getCursorOfAllUtilisateurs();

        }

    }*/

    //ABONNEMENT IMPLEMENTATIONS

    public static Cursor getCursorCurrentAbonnementsOfAnUtilisateur(Long utilisateur_id) throws InterruptedException, ExecutionException {
        Cursor cursor = null;
        try {
            AsyncTask<Long, Void, Cursor> task = new GetCursorCurrentAbonnementsOfAnUtilisateur(RetrieveMyApplicationContext.getInstance()).execute(utilisateur_id);
            cursor = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

        return cursor;
    }

    public static Long addAbonnement(Abonnement abonnement) throws InterruptedException, ExecutionException{
        long result = -1L;
        try {
            AsyncTask<Abonnement, Void, Long> task = new AddAbonnementAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(abonnement);
            result = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

        return result;
    }

    public static Abonnement selectionnerAbonnement(long id) throws InterruptedException, ExecutionException{
        Abonnement abonnement = null;
        try {
            AsyncTask<Long, Void, Abonnement> task = new SelectAbonnementAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(id);
            abonnement = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

        return abonnement;

    }

    public static void removeAbonnement(long id){

        try {
            new RemoveAbonnementAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

    }

    public static Abonnement getCurrentAbonnementsOfAnUtilisateur(Long utilisateur_id) throws InterruptedException, ExecutionException {
        Abonnement abonnement = null;
        try {
            AsyncTask<Long, Void, Abonnement> task = new GetCurrentAbonnementsOfAnUtilisateur(RetrieveMyApplicationContext.getInstance()).execute(utilisateur_id);
            abonnement = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

        return abonnement;
    }

    public static void modifierAbonnement(ContentValues values) {

        try {
            AsyncTask<ContentValues, Void, Void> task = new UpdateAbonnementAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(values);
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

    }

    private static class AddAbonnementAsyncTask extends AsyncTask<Abonnement, Void, Long>{

        private WeakReference<AbonnementDao> abonnementDao;
        private WeakReference<Context> mContext;

        AddAbonnementAsyncTask( Context context){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.abonnementDao = new WeakReference<>(
                    new AbonnementDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Long doInBackground(Abonnement... abonnements) {
            return abonnementDao.get().ajouter(abonnements[0]);
        }

    }

    /**
     * May return Abonnement equals null if Exception
     */
    private static class SelectAbonnementAsyncTask extends AsyncTask<Long, Void, Abonnement>{

        private WeakReference<AbonnementDao> abonnementDao;
        private WeakReference<Context> mContext;

        SelectAbonnementAsyncTask( Context context){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.abonnementDao = new WeakReference<>(
                    new AbonnementDao(context));
            this.mContext = new WeakReference<>(
                    context);

        }

        @Override
        protected Abonnement doInBackground(Long... longs) {
            Abonnement abonnement = new Abonnement();
            try {
                abonnement = abonnementDao.get().selectionner(longs[0]);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return abonnement;
        }

    }

    private static class RemoveAbonnementAsyncTask extends AsyncTask<Long, Void, Void>{

        private WeakReference<AbonnementDao> abonnementDao;
        private WeakReference<Context> mContext;

        RemoveAbonnementAsyncTask( Context context){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.abonnementDao = new WeakReference<>(
                    new AbonnementDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Void doInBackground(Long... longs) {
            abonnementDao.get().supprimer(longs[0]);

            return null;
        }

    }


    private static class GetCursorCurrentAbonnementsOfAnUtilisateur extends AsyncTask<Long, Void, Cursor>{

        private WeakReference<AbonnementDao> abonnementDao;
        private WeakReference<Context> mContext;

        GetCursorCurrentAbonnementsOfAnUtilisateur( Context context){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.abonnementDao = new WeakReference<>(
                    new AbonnementDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Cursor doInBackground(Long... longs) {
            return abonnementDao.get().getCursorCurrentAbonnementsOfAnUtilisateur(longs[0]);
        }

    }

    private static class GetCurrentAbonnementsOfAnUtilisateur extends AsyncTask<Long, Void, Abonnement>{

        private WeakReference<AbonnementDao> abonnementDao;
        private WeakReference<Context> mContext;

        GetCurrentAbonnementsOfAnUtilisateur( Context context){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.abonnementDao = new WeakReference<>(
                    new AbonnementDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Abonnement doInBackground(Long... longs) {
            return abonnementDao.get().getCurrentAbonnementsOfAnUtilisateur(longs[0]);
        }

    }

    private static class UpdateAbonnementAsyncTask extends AsyncTask<ContentValues, Void, Void>{

        private WeakReference<AbonnementDao> abonnementDao;
        private WeakReference<Context> mContext;

        UpdateAbonnementAsyncTask( Context context){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.abonnementDao = new WeakReference<>(
                    new AbonnementDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            abonnementDao.get().modifier(contentValues[0]);

            return null;
        }

    }


    //Codes Implementations

    public static Long addCode(Code_Securite_Transactions code)throws InterruptedException, ExecutionException {
        long result = -1L;
        try {
            AsyncTask<Code_Securite_Transactions, Void, Long> task = new AddCodeAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(code);
            result = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

        return result;
    }

    public static ArrayList<Code_Securite_Transactions> selectionnerTousCodes(long id_utilisateur) throws InterruptedException, ExecutionException{
        ArrayList<Code_Securite_Transactions> codes = null;
        try {
            AsyncTask<Long, Void, ArrayList<Code_Securite_Transactions>> task = new SelectAllCodesAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(id_utilisateur);
            codes = task.get();
        } catch (NullPointerException e) {
            e.printStackTrace();
            if(ErrorActivity.currentActivity.get() != null) {
                Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ErrorActivity.currentActivity.get().startActivity(intent);
                ErrorActivity.currentActivity.get().finish();
            }
        }

        return codes;
    }

    private static class AddCodeAsyncTask extends AsyncTask<Code_Securite_Transactions, Void, Long>{

        private WeakReference<CodesDao> codesDao;
        private WeakReference<Context> mContext;

        AddCodeAsyncTask( Context context ){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.codesDao = new WeakReference<>(
                    new CodesDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Long doInBackground(Code_Securite_Transactions... code_securite_transactionses) {
            return codesDao.get().ajouter(code_securite_transactionses[0]);
        }

    }

    /**
     */
    private static class SelectAllCodesAsyncTask extends AsyncTask<Long, Void, ArrayList<Code_Securite_Transactions>>{

        private WeakReference<CodesDao> codesDao;
        private WeakReference<Context> mContext;

        SelectAllCodesAsyncTask( Context context ){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.codesDao = new WeakReference<>(
                    new CodesDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected ArrayList<Code_Securite_Transactions> doInBackground(Long... longs) {

            return codesDao.get().selectionnerTousLesCodes(longs[0]);
        }

    }

    private static class RemoveCodeAsyncTask extends AsyncTask<Long, Void, Void> {

        private WeakReference<CodesDao> codesDao;
        private WeakReference<Context> mContext;

        RemoveCodeAsyncTask( Context context ){
            if(context == null){
                context = ErrorActivity.currentActivity.get();
                if(context == null){
                    context = RetrieveMyApplicationContext.getAppContext();
                    if(context == null) {
                        context = new Activity();
                    }
                }
            }
            this.codesDao = new WeakReference<>(
                    new CodesDao(context));
            this.mContext = new WeakReference<>(
                    context);
        }

        @Override
        protected Void doInBackground(Long... longs) {
            codesDao.get().supprimer(longs[0]);

            return null;
        }
    }


        //Parametres Implementations

        public static void modifierParametres(ContentValues values) {

            try {
                AsyncTask<ContentValues, Void, Void> task = new UpdateParametresAsyncTask(RetrieveMyApplicationContext.getInstance()).execute(values);
            } catch (NullPointerException e) {
                e.printStackTrace();
                if(ErrorActivity.currentActivity.get() != null) {
                    Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ErrorActivity.currentActivity.get().startActivity(intent);
                    ErrorActivity.currentActivity.get().finish();
                }
            }
        }

        public static Parametres selectionnerParametres() throws InterruptedException, ExecutionException{
            Parametres parametres = null;
            try {
                AsyncTask<Void, Void, Parametres> task = new SelectParametresAsyncTask(RetrieveMyApplicationContext.getInstance()).execute();
                parametres = task.get();
            } catch (NullPointerException e) {
                e.printStackTrace();
                if(ErrorActivity.currentActivity.get() != null) {
                    Intent intent = new Intent(ErrorActivity.currentActivity.get(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ErrorActivity.currentActivity.get().startActivity(intent);
                    ErrorActivity.currentActivity.get().finish();
                }
            }

            return parametres;
        }

        private static class UpdateParametresAsyncTask extends AsyncTask<ContentValues, Void, Void>{

            private WeakReference<ParametresDao> parametresDao;
            private WeakReference<Context> mContext;

            UpdateParametresAsyncTask( Context context){
                if(context == null){
                    context = ErrorActivity.currentActivity.get();
                    if(context == null){
                        context = RetrieveMyApplicationContext.getAppContext();
                        if(context == null) {
                            context = new Activity();
                        }
                    }
                }
                this.parametresDao = new WeakReference<>(
                        new ParametresDao(context));
                this.mContext = new WeakReference<>(
                        context);
            }

            @Override
            protected Void doInBackground(ContentValues... contentValues) {
                parametresDao.get().modifier(contentValues[0]);

                return null;
            }

        }

        /**
         */
        private static class SelectParametresAsyncTask extends AsyncTask<Void, Void, Parametres>{

            private WeakReference<ParametresDao> parametresDao;
            private WeakReference<Context> mContext;

            SelectParametresAsyncTask( Context context){
                if(context == null){
                    context = ErrorActivity.currentActivity.get();
                    if(context == null){
                        context = RetrieveMyApplicationContext.getAppContext();
                        if(context == null) {
                            context = new Activity();
                        }
                    }
                }
                this.parametresDao = new WeakReference<>(
                        new ParametresDao(context));
                this.mContext = new WeakReference<>(
                        context);
            }

            @Override
            protected Parametres doInBackground(Void... voids) {

                return parametresDao.get().selectionnerParametres();
            }

        }

}
