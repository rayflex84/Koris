package com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations;

import android.content.Intent;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.torepofficiel.carem.koris.BienvenueActivity;
import com.torepofficiel.carem.koris.LoginActivity;
import com.torepofficiel.carem.koris.PlaceholderFragment;
import com.torepofficiel.carem.koris.R;
import com.torepofficiel.carem.koris.RegistrationActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Abonnement;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.custom_volley.CustomStringRequest;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by macbook on 15/09/17.
 */

public class ConnexionCheckingAsyncTask extends AsyncTask<Void,Void,Boolean> {


    private int which;
    public ConnexionCheckingAsyncTask(int which) {
        this.which = which;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        switch (which){
            case 1:
                if(PlaceholderFragment.getInstance().showingRefreshProgress) {
                    PlaceholderFragment.getInstance().showingRefreshProgress
                            = PlaceholderFragment.getInstance().showRefreshProgress(false);;
                }

                RegistrationActivity.getInstance().checkingConnexionState(aBoolean);
                break;

            case 2:
                if (!aBoolean) {

                    if (RegistrationActivity.getInstance().showingProgressbar) {
                        BienvenueActivity.getInstance().showingRegisterProgress
                                = RegistrationActivity.getInstance().showRegisterProgress(false);
                    }
                    RegistrationActivity.getInstance().showAlertDialog(-1, RegistrationActivity.getInstance().getString(R.string.titre_dialog_cnx_checking),
                            RegistrationActivity.getInstance().getString(R.string.msg_dialog_cnx_checking, "vous enregistrer"),
                            R.mipmap.logo_small, RegistrationActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), "").setCancelable(false);
                } else {

                    final RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.getInstance().getApplicationContext());
                    String url = "https://torepofficiel.com/web/main_controlleur.php/new/utilisateur/koris";

                    final Utilisateur newUtilisateur = RegistrationActivity.getInstance().getPlaceholderFragment().getNewUtilisateur();

                    HashMap<String, String> params = ClasseUtilitaire.newUtilisateurToMap(
                                                        newUtilisateur
                                                    );

                    final CustomStringRequest stringRequestRequest = new CustomStringRequest(Request.Method.POST, url, params, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            requestQueue.stop();

                            Abonnement newAbonnement = new Abonnement().setEst_null(false);

                            if(response.equals("\"1\"")){
                                try {
                                    DatabaseAsyncTask.addUtilisateur(newUtilisateur);
                                    DatabaseAsyncTask.addAbonnement(newAbonnement);
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }

                                newUtilisateur.setHisAbonnement(newAbonnement);

                                ClasseUtilitaire.writeAnObjectInFile(newUtilisateur);

                                newUtilisateur.release();

                                Intent intent = new Intent(RegistrationActivity.getInstance(), LoginActivity.class);
                                intent.putExtra(RegistrationActivity.CONNECTE, true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                RegistrationActivity.getInstance().startActivity(intent);
                                RegistrationActivity.getInstance().finish();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            requestQueue.stop();

                            RegistrationActivity.getInstance().showingProgressbar
                                    = RegistrationActivity.getInstance().showRegisterProgress(false);

                            RegistrationActivity.getInstance().showAlertDialog(-1, "Oopss !",
                                    RegistrationActivity.getInstance().getString(R.string.prob_connexion),
                                    R.mipmap.logo_small, RegistrationActivity.getInstance().getString(R.string.ok_dialog_cnx_checking), "").setCancelable(false);
                        }
                    });

                    new android.os.Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            requestQueue.add(stringRequestRequest);
                        }
                    });

                }
                break;
        }

    }

    @Override
    protected void onCancelled() {

        if(PlaceholderFragment.getInstance().showingRefreshProgress) {
            PlaceholderFragment.getInstance().showingRefreshProgress
                    = PlaceholderFragment.getInstance().showRefreshProgress(false);;
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        return ClasseUtilitaire.hasActiveInternetConnection();

    }



}
