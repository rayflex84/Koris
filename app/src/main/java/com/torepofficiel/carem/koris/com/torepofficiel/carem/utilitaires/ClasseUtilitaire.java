package com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.MainActivity;
import com.torepofficiel.carem.koris.R;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.DatabaseAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.DialogActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.MyAlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class ClasseUtilitaire {

    private static final String TEL_MOBILE_PATTERN = "^(00228){0,1}[97][0-9]{1,1}([0-9][0-9]){3,3}$"; //"^(((0{2}|\\+)(228))?([\\s]?9|7[0-9]))([\\s-]?[0-9]{2}){3}";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\\.[a-z]{2,6}$";
    private static final String pattern1 = "(^[\\w\\déèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*([A-Z][\\wéèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*)+(\\d[\\w\\déèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*)+[\\w\\déèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*$)";
    private static final String pattern2 = "(^[\\w\\déèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*(\\d[\\w\\déèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*)+([A-Z][\\wéèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*)+[\\w\\déèêëàîïûüùôö^&%#@*!+=.,;:<>_-]*$)";
    public static final int TIME_INTERVAL = 2000;

    public static boolean checkSimChange(Context context){

        String userSubscriberId = getUserSubscriberId();

        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = DatabaseAsyncTask.selectionnerUtilisateur(1L);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        String userSubscriberIdRegistered = utilisateur.getSubscriber_id();

        if(userSubscriberIdRegistered == null || userSubscriberId == null || !userSubscriberId.equals(userSubscriberIdRegistered) ){
            Intent intent1 = new Intent(context, ErrorActivity.class);
            intent1.putExtra(MainActivity.SIM_CHANGEMENT, context.getString(R.string.sim_changement));
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent1);
            return true;
        }
        return false;
    }

    public static String getUserSubscriberId(){
        TelephonyManager telephonyManager;

        try {
            telephonyManager = (TelephonyManager) ErrorActivity.currentActivity.get().
                    getSystemService(TELEPHONY_SERVICE);
        } catch (NullPointerException e) {
            e.printStackTrace();
            telephonyManager = (TelephonyManager) ErrorActivity.currentActivity.get().
                    getSystemService(TELEPHONY_SERVICE);
        }

        return telephonyManager.getSubscriberId();
    }

    public static boolean isEmptyAndBlank(String charSequence) {
        if(charSequence == null) return true;
        return charSequence.matches("^\\s*$");
    }

    public static boolean checkEditTextLength(String charSequence, int minLength) {
        return charSequence.length() < minLength;
    }

    public static boolean checkEmailValidation(String charSequence) {
        return !charSequence.matches(EMAIL_PATTERN);
    }

    public static boolean checkPhoneValidation(String charSequence) {
        return !charSequence.matches(TEL_MOBILE_PATTERN);
    }

    public static String mot_de_passe_validation(Context context, String mdp, int minLength){

        if(checkEditTextLength(mdp, minLength)) return context.getString(R.string.error_invalid_password, minLength);

        return ( (mdp.matches( pattern1 ) || mdp.matches( pattern2 ) ) ? "" : context.getString(R.string.error_incorrect_password) );

    }

    public static View.OnFocusChangeListener getOnFocusListener(final TextView textView, final Context context, final int currentTextView) {
        return (new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    textView.setVisibility(View.VISIBLE);
                    ((EditText) view).setHint("");
                } else {
                    if (((EditText) view).getText().toString().isEmpty()) {
                        textView.setVisibility(View.GONE);
                        ((EditText) view).setHint((currentTextView == 0) ? R.string.prompt_password : R.string.confirm_mdp);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.color_darker_gray));
                    }

                }
            }
        });
    }

    /*public static String getPostDataString(Utilisateur utilisateur) throws Exception {

        String string_params = "";
        Gson gson = new Gson();
        try {
            string_params = gson.toJson(utilisateur);
        } catch (Exception e) {
            e.printStackTrace();
        }

        *//*params.put("action", "sign_in");
        params.put("nom", utilisateur.getNom());
        params.put("prenom", utilisateur.getPrenoms());
        params.put("email", utilisateur.getEmail());
        params.put("naissance", utilisateur.getDate_naissance());
        params.put("date_inscription", utilisateur.getDate_inscription());
        params.put("subscriber_id", utilisateur.getSubscriber_id());
        params.put("mot_de_passe", utilisateur.getMot_de_passe());
        params.put("numero", utilisateur.getNumero());
        params.put("ville", utilisateur.getVille());
        params.put("solde_compte_local", 0);
        params.put("solde_compte_ligne", 0);
        params.put("solde_compte_epargne_local", 0);
        params.put("solde_compte_epargne_ligne", 0);
        params.put("type_compte", utilisateur.getType_compte());
        params.put("hasNotifiedPhoneChange", utilisateur.getHasNotifiedPhoneChange());
        params.put("id_card", utilisateur.getIdCard());
        params.put("code_securite_transactions", utilisateur.getCode_securite_transactions());
        params.put("est_abonne", utilisateur.getEstAbonne());*//*

        StringBuilder result = new StringBuilder();
        boolean first = true;

        JSONObject params = new JSONObject(string_params);

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }*/

    /*public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }*/

    public static HashMap<String, String> utilisateurToMap(Utilisateur utilisateur) {
        HashMap<String, String> map;

        map = newUtilisateurToMap(utilisateur);

        map.put("solde_compte_local", utilisateur.getSolde_compte_local().toString());
        map.put("solde_compte_ligne", utilisateur.getSolde_compte_ligne().toString());
        map.put("solde_compte_epargne_local", utilisateur.getSolde_compte_epargne_local().toString());
        map.put("solde_compte_epargne_ligne", utilisateur.getSolde_compte_epargne_ligne().toString());
        map.put("has_notified_phone_change", String.valueOf(utilisateur.getHas_notified_phone_change()));
        map.put("est_abonne", String.valueOf(utilisateur.getEst_abonne()));
        map.put("app_freezed", String.valueOf(utilisateur.isApp_freezed()));

        return map;
    }

    public static HashMap<String, String> newUtilisateurToMap(Utilisateur utilisateur) {
        HashMap<String, String> map = new HashMap<>();
        map.put("nom", utilisateur.getNom());
        map.put("prenom", utilisateur.getPrenom());
        map.put("email", utilisateur.getEmail());
        map.put("naissance", utilisateur.getNaissance().toString().substring( 0, 19 ));
        map.put("date_inscription", utilisateur.getDate_inscription().toString().substring( 0, 19 ));
        map.put("subscriber_id", utilisateur.getSubscriber_id());
        map.put("mot_de_passe", utilisateur.getMot_de_passe());
        map.put("numero", utilisateur.getNumero());
        map.put("ville", utilisateur.getVille());
        map.put("type_compte", utilisateur.getType_compte());
        map.put("id_card", utilisateur.getId_card());

        return map;
    }

    public static boolean checkAutotime() {

        Context context = RetrieveMyApplicationContext.getAppContext();
        ContentResolver contentResolver = context.getContentResolver();

        final DialogActivity activity = ((DialogActivity)ErrorActivity.currentActivity.get());

        boolean result = true;

        if (Build.VERSION.SDK_INT < 17) {
            if (Settings.System.getInt(contentResolver, Settings.Global.AUTO_TIME, 0) < 1 ||
                    Settings.System.getInt(contentResolver, Settings.Global.AUTO_TIME_ZONE, 0) < 1) {

                result = false;
                MyAlertDialog.choix = 1;

                new android.os.Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        activity.showAlertDialog(-1, activity.getString(R.string.titre_auto_time),
                                activity.getString(R.string.msg_auto_time),
                                R.mipmap.logo_small, activity.getString(R.string.ok_dialog_cnx_checking), "").setCancelable(false);
                    }
                });

            }
        } else {
            if (Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME, 0) < 1 ||
                    Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME_ZONE, 0) < 1) {

                result = false;
                MyAlertDialog.choix = 1;

                new android.os.Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        activity.showAlertDialog(-1, activity.getString(R.string.titre_auto_time),
                                activity.getString(R.string.msg_auto_time),
                                R.mipmap.logo_small, activity.getString(R.string.ok_dialog_cnx_checking), "").setCancelable(false);
                    }
                });

            }
        }

        return result;
    }

    public static void writeAnObjectInFile(Utilisateur utilisateur) {
        File rootDirectory = new File(MainActivity.ROOT_DIRECTORY);
        File utilisateurFile = new File(rootDirectory, MainActivity.UTILISATEUR_FILE);

        if(utilisateurFile.exists())
            utilisateurFile.delete();

        rootDirectory.mkdirs();
        utilisateurFile.setReadOnly();

        try {
            utilisateurFile.createNewFile();
            FileOutputStream ostream = new FileOutputStream(utilisateurFile);
            ObjectOutputStream os = new ObjectOutputStream(ostream);
            os.writeObject(utilisateur);
            ostream.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Utilisateur readAnObjectInFile() {
        File rootDirectory = new File(MainActivity.ROOT_DIRECTORY);
        File utilisateurFile = new File(rootDirectory, MainActivity.UTILISATEUR_FILE);
        Utilisateur utilisateur = new Utilisateur();
        try {
            FileInputStream instream = new FileInputStream(utilisateurFile);
            ObjectInputStream ins = new ObjectInputStream(instream);
            utilisateur = (Utilisateur) ins.readObject();
            instream.close();
            ins.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return utilisateur;
    }

    public static void showToast(Context context, String message, int duration){

        int d = (duration == -1 ) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

        Toast.makeText(context,message, d).show();
    }

    private static boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ErrorActivity.currentActivity.get().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public static boolean hasActiveInternetConnection(){
        if(isNetworkAvailable()){
            try{
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return false;
    }

    public static String numberFormatted(long number){

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }

}
