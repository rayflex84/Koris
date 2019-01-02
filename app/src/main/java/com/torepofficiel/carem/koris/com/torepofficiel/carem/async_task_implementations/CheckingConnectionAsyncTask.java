package com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations;

/**
 * Created by macbook on 26/08/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.torepofficiel.carem.koris.BienvenueActivity;
import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.LoginActivity;
import com.torepofficiel.carem.koris.R;
import com.torepofficiel.carem.koris.RegistrationActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Abonnement;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.custom_volley.CustomJSonObjectRequest;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.receivers.NotifiyCnxStateReiceiver;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.GsonDateFormatter;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.RetrieveMyApplicationContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class CheckingConnectionAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private boolean action = false;

    private WeakReference<Context> mContext;

    public CheckingConnectionAsyncTask(boolean action) {
        this.action = action;
        mContext = new WeakReference<>(RetrieveMyApplicationContext.getAppContext());
        if (mContext == null)
            mContext = new WeakReference<>(ErrorActivity.currentActivity.get().getApplicationContext());
        if (mContext == null)
            mContext = new WeakReference<>((Context) ErrorActivity.currentActivity.get());
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (action) {
            String errorMsg = "pouvoir créer un compte";
            if (!aBoolean) {

                if (BienvenueActivity.getInstance().showingRegisterProgress) {
                    BienvenueActivity.getInstance().showingRegisterProgress
                            = BienvenueActivity.getInstance().showRegisterProgress(false);
                }
                BienvenueActivity.getInstance().showAlertDialog(-1, BienvenueActivity.getInstance().getString(R.string.titre_dialog_cnx_checking),
                        BienvenueActivity.getInstance().getString(R.string.msg_dialog_cnx_checking, errorMsg),
                        R.mipmap.logo_small, BienvenueActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), "").setCancelable(false);
            } else {

                    final RequestQueue requestQueue = Volley.newRequestQueue(BienvenueActivity.getInstance().getApplicationContext());
                    String url = "https://torepofficiel.com/web/main_controlleur.php/verify/utilisateur/account/" + ClasseUtilitaire.getUserSubscriberId();

                    HashMap<String, String> params = new HashMap<>();
                    final CustomJSonObjectRequest jsonObjectRequest = new CustomJSonObjectRequest(url, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            requestQueue.stop();

                            try {

                                if (response.getInt("success_utilisateur") == 1) {

                                    Utilisateur utilisateur;
                                    Abonnement abonnement = new Abonnement();
                                    JSONObject util = response.getJSONObject("utilisateur");
                                    Gson gson = new GsonBuilder().registerTypeAdapter(java.util.Date.class,
                                            new GsonDateFormatter()).create();
                                    utilisateur = gson.fromJson(util.toString(), Utilisateur.class);

                                    if (response.getInt("success_abonnement") == 1) {
                                        JSONObject abon = response.getJSONObject("abonnement");
                                        abonnement = gson.fromJson(abon.toString(), Abonnement.class);
                                        abonnement.setEst_null(false);
                                    }

                                    ContentValues values = new ContentValues();
                                    values.put(DataBaseHandler.SAUVEGARDE_INTERNET, new Timestamp(System.currentTimeMillis()).toString());

                                    DatabaseAsyncTask.modifierParametres(values);

                                    try {
                                        DatabaseAsyncTask.addUtilisateur(utilisateur);
                                        if (abonnement != null && !abonnement.isEst_null())
                                            DatabaseAsyncTask.addAbonnement(abonnement);
                                    } catch (InterruptedException | ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                    utilisateur.setHisAbonnement(abonnement);

                                    ClasseUtilitaire.writeAnObjectInFile(utilisateur);

                                    Intent intent = new Intent(BienvenueActivity.getInstance(), LoginActivity.class);
                                    intent.putExtra(BienvenueActivity.COMPTE_CHARGE,true);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    BienvenueActivity.getInstance().startActivity(intent);
                                    BienvenueActivity.getInstance().finish();

                                    utilisateur.release();
                                    utilisateur = null;
                                } else {

                                    Intent intent = new Intent(BienvenueActivity.getInstance(), RegistrationActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    BienvenueActivity.getInstance().startActivity(intent);
                                    BienvenueActivity.getInstance().finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            requestQueue.stop();

                            BienvenueActivity.getInstance().showingRegisterProgress
                                    = BienvenueActivity.getInstance().showRegisterProgress(false);

                            BienvenueActivity.getInstance().showAlertDialog(-1, "Oopss !",
                                    BienvenueActivity.getInstance().getString(R.string.prob_connexion),
                                    R.mipmap.logo_small, BienvenueActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), "").setCancelable(false);
                        }
                    });

                    new android.os.Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            requestQueue.add(jsonObjectRequest);
                        }
                    });

            }

        } else {
            NotifiyCnxStateReiceiver.etat = aBoolean;
        }

    }

    @Override
    protected void onCancelled() {

        if (BienvenueActivity.getInstance().showingRegisterProgress) {
            BienvenueActivity.getInstance().showingRegisterProgress
                    = BienvenueActivity.getInstance().showRegisterProgress(false);
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ClasseUtilitaire.hasActiveInternetConnection();
            /*try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;*/
    }

}
