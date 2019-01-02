package com.torepofficiel.carem.koris;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.DatabaseAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Abonnement;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.dialog.MyAlertDialog;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.Calendrier;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import static com.torepofficiel.carem.koris.KorisActivity.SOLDE_LOCAL;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabFragment2 extends Fragment {

    TextView prix_abonnement,
            pas_abonne,
            duree,
            debut,
            fin;
    Button pay_by_local,
            pay_by_online;

    RelativeLayout abonne;

    private static WeakReference<TabFragment2> tabFragment2;

    long abonnement_price;
    String type_abonnement;
    long duree_abonnement;
    int[] values_6months_abonnement = null;

    RelativeLayout abonnement_progressbar;
    ProgressBar mProgressBar;
    public MyAlertDialog myAlertDialog;

    public TabFragment2() {
    }

    public static TabFragment2 getInstance(){
        return (tabFragment2 != null) ? tabFragment2.get() : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                View rootView = inflater.inflate(R.layout.fragment_koris_abonnement, container, false);

        tabFragment2 = new WeakReference<>(this);

                Spinner liste_abonnements = rootView.findViewById(R.id.abonnements);
                prix_abonnement = rootView.findViewById(R.id.prix_abonnement);
                prix_abonnement.setText(R.string.prix_200);
                abonnement_price = 200;
                type_abonnement = "Une journée(24h)";
                duree_abonnement = 86_400_000L;

                pas_abonne = rootView.findViewById(R.id.no_abonnement);
                abonnement_progressbar = rootView.findViewById(R.id.abonnement_progress);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(KorisActivity.getInstance(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                duree = rootView.findViewById(R.id.duree);
                debut = rootView.findViewById(R.id.debut);
                fin = rootView.findViewById(R.id.fin);
                pay_by_local = rootView.findViewById(R.id.but_pay_by_local);
                pay_by_online = rootView.findViewById(R.id.pay_by_online);
                setRoundedButtonBackground(pay_by_local, pay_by_online);

                abonne = rootView.findViewById(R.id.abonne);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ErrorActivity.currentActivity.get(),
                        R.array.abonnements, R.layout.spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                liste_abonnements.setAdapter(adapter);

                liste_abonnements.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                        if(view != null) {
                            type_abonnement = ((TextView) view).getText().toString();

                            switch (pos) {
                                case 0:
                                    prix_abonnement.setText(R.string.prix_200);
                                    abonnement_price = 200;
                                    duree_abonnement = 86_400_000L;
                                    values_6months_abonnement = null;
                                    break;

                                case 1:
                                    prix_abonnement.setText(R.string.prix_3000);
                                    abonnement_price = 3000;
                                    duree_abonnement = getPeriodeByDaysInMonths(Calendrier.getCurrentMonth());
                                    values_6months_abonnement = null;
                                    break;

                                case 2:
                                    prix_abonnement.setText(R.string.prix_17000);
                                    abonnement_price = 17000;
                                    values_6months_abonnement = get6MonthsPeriod();
                                    break;
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                pay_by_local.setOnClickListener(getBut_local_clickListener());

        pay_by_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchViews(2);

                if(ClasseUtilitaire.checkAutotime()){
                    MyAlertDialog.choix = 5;
                    MyAlertDialog.choixNegatif = 1;
                    myAlertDialog = KorisActivity.getInstance().showAlertDialog(-1, KorisActivity.getInstance().getString(R.string.confirmation),
                            KorisActivity.getInstance().getString(R.string.msg_confirmation),
                            R.mipmap.logo_small, KorisActivity.getInstance().getString(R.string.confirmer_dialog_cnx_checking), KorisActivity.getInstance().getString(R.string.annuler_dialog_cnx_checking));
                    myAlertDialog.setCancelable(false);

                } else
                    switchViews(1);
            }
        });

        refreshAbonnementFragment();



        return rootView;
    }

    private View.OnClickListener getBut_local_clickListener(){
        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchViews(2);

                        if(ClasseUtilitaire.checkAutotime()){
                            MyAlertDialog.choix = 4;
                            MyAlertDialog.choixNegatif = 1;
                            myAlertDialog = KorisActivity.getInstance().showAlertDialog(-1, KorisActivity.getInstance().getString(R.string.confirmation),
                                    KorisActivity.getInstance().getString(R.string.msg_confirmation),
                                    R.mipmap.logo_small, KorisActivity.getInstance().getString(R.string.confirmer_dialog_cnx_checking), KorisActivity.getInstance().getString(R.string.annuler_dialog_cnx_checking));
                            myAlertDialog.setCancelable(false);

                        } else
                            switchViews(1);
                    }
        });
    }

    public void pay_abonnement_local_account(){
        myAlertDialog.dismiss();

        Utilisateur utilisateur = new Utilisateur();

        try {
            utilisateur = DatabaseAsyncTask.selectionnerUtilisateur(1L);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        long soldeLocal = utilisateur.getSolde_compte_local();

        if(soldeLocal < abonnement_price){

            new AlertDialog.Builder(KorisActivity.getInstance())
                    .setTitle(R.string.solde_insuffisant_operation_canceled)
                    .setMessage(KorisActivity.getInstance().getString(R.string.msg_solde_insuffisant_operation_canceled, "local"))
                    .setIcon(R.mipmap.logo_small)
                    .setPositiveButton(R.string.ok_dialog_cnx_checking, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switchViews(1);
                        }
                    })
                    .create()
            .show();


        } else {
            utilisateur.setSolde_compte_local(soldeLocal - abonnement_price);
            newAbonnementProcess(utilisateur, true);

        }
    }

    private void newAbonnementProcess(Utilisateur utilisateur, boolean is_local_account){
        utilisateur.setEst_abonne(true);
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp date_debut = new Timestamp(currentTimeMillis),
                date_fin;
        if(values_6months_abonnement != null){
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(currentTimeMillis);
            calendar.set(values_6months_abonnement[2], values_6months_abonnement[1] - 1, values_6months_abonnement[0]);
            date_fin = new Timestamp(calendar.getTimeInMillis());
        }else
            date_fin = new Timestamp(currentTimeMillis + duree_abonnement);

        Abonnement newAbonnememt = new Abonnement(type_abonnement, date_debut, abonnement_price, date_fin, 1L, true);

        Utilisateur utilisateur_in_file = ClasseUtilitaire.readAnObjectInFile();

        utilisateur_in_file.setHisAbonnement(newAbonnememt);
        utilisateur_in_file.setEst_abonne(true);

        ClasseUtilitaire.writeAnObjectInFile(utilisateur_in_file);

        ContentValues utilisateurValue = new ContentValues();
        if(is_local_account)
            utilisateurValue.put(DataBaseHandler.SOLDE_COMPTE_LOCAL, utilisateur.getSolde_compte_local());
        utilisateurValue.put(DataBaseHandler.EST_ABONNE, utilisateur.getEst_abonne());

        ContentValues abonnementValue = new ContentValues();
        abonnementValue.put(DataBaseHandler.TYPE_ABONNEMENT, newAbonnememt.getType_abonnement());
        abonnementValue.put(DataBaseHandler.DATE_ABONNEMENT, newAbonnememt.getDate_abonnement().toString());
        abonnementValue.put(DataBaseHandler.MONTANT_ABONNEMENT, newAbonnememt.getMontant_abonnement());
        abonnementValue.put(DataBaseHandler.DATE_FIN_ABONNEMENT, newAbonnememt.getDate_fin_abonnement().toString());
        abonnementValue.put(DataBaseHandler.EST_ACTIF, newAbonnememt.isEst_actif());

        DatabaseAsyncTask.updateUtilisateur(utilisateurValue);
        DatabaseAsyncTask.modifierAbonnement(abonnementValue);

        utilisateur.setHisAbonnement(newAbonnememt);

        try {
            utilisateur.setListeCode( DatabaseAsyncTask.selectionnerTousCodes(
                    1L )
            );

            utilisateur.setApp_freezed(DatabaseAsyncTask.selectionnerParametres()
                    .isApp_freezed());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ClasseUtilitaire.writeAnObjectInFile(utilisateur);
        String periode = "";

        switch ((int)abonnement_price){
            case 200:
                periode = "d'une journée (24h)\n";
                break;

            case 3000:
                periode = "d'un mois\n";
                break;

            case 17000:
                periode = "de 6mois\n";
                break;
        }

        new AlertDialog.Builder(KorisActivity.getInstance())
                .setTitle(R.string.operation_reussie)
                .setMessage(KorisActivity.getInstance().getString(R.string.msg_abonnement_reussi, periode))
                .setIcon(R.mipmap.logo_small)
                .setPositiveButton(R.string.ok_dialog_cnx_checking, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        refreshAbonnementFragment();
                    }
                })
                .create()
                .show();

        KorisActivity.utilisateur_bundle.putLong(SOLDE_LOCAL, utilisateur.getSolde_compte_local());
    }

    public void pay_abonnement_online_account(){

        myAlertDialog.dismiss();

        final RequestQueue requestQueue = Volley.newRequestQueue(KorisActivity.getInstance().getApplicationContext());
        String url = "https://torepofficiel.com/web/main_controlleur.php/koris/new/abonnement/" + ClasseUtilitaire.getUserSubscriberId() + "/" + abonnement_price;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                requestQueue.stop();

                if (response.equals("\"0\"")){
                    new AlertDialog.Builder(KorisActivity.getInstance())
                            .setTitle(R.string.solde_insuffisant_operation_canceled)
                            .setMessage(KorisActivity.getInstance().getString(R.string.msg_solde_insuffisant_operation_canceled, "en ligne"))
                            .setIcon(R.mipmap.logo_small)
                            .setPositiveButton(R.string.ok_dialog_cnx_checking, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switchViews(1);
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    switchViews(1);
                                }
                            })
                            .create()
                            .show();
                } else {
                    try {
                        newAbonnementProcess(DatabaseAsyncTask.selectionnerUtilisateur(1L),false);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.stop();

                new AlertDialog.Builder(KorisActivity.getInstance())
                        .setTitle(R.string.titre_connexion_interrompue)
                        .setMessage(R.string.prob_connexion)
                        .setIcon(R.mipmap.logo_small)
                        .setPositiveButton(R.string.ok_dialog_cnx_checking, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switchViews(1);
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                switchViews(1);
                            }
                        })
                        .create()
                        .show();

            }
        });

        new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {
                requestQueue.add(stringRequest);
            }
        });
    }

    public void refreshAbonnementFragment() {

        Abonnement currentAbonnememt = new Abonnement();

        try {
            currentAbonnememt = DatabaseAsyncTask.selectionnerAbonnement(1L);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if(!currentAbonnememt.isEst_null()){

            if(currentAbonnememt.isEst_actif()){
                pay_by_local.setEnabled(false);
                pay_by_online.setEnabled(false);
                switchViews(0);
                duree.setText(currentAbonnememt.getType_abonnement());
                debut.setText(Calendrier.timestampInLitteral(currentAbonnememt.getDate_abonnement()));
                fin.setText(Calendrier.timestampInLitteral(currentAbonnememt.getDate_fin_abonnement()));

            } else {
                pay_by_local.setEnabled(true);
                pay_by_online.setEnabled(true);
                switchViews(1);
            }

        }

    }

    public void switchViews(int how){
        switch (how){
            case 0:
                pas_abonne.setVisibility(View.GONE);
                abonne.setVisibility(View.VISIBLE);
                abonnement_progressbar.setVisibility(View.GONE);
                break;

            case 1:
                pas_abonne.setVisibility(View.VISIBLE);
                abonne.setVisibility(View.GONE);
                abonnement_progressbar.setVisibility(View.GONE);
                break;

            case 2:
                pas_abonne.setVisibility(View.GONE);
                abonne.setVisibility(View.GONE);
                abonnement_progressbar.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setRoundedButtonBackground(Button... buttons){
        for(Button button : buttons)
            button.setBackgroundResource(R.drawable.custom_solde_background);
    }

    private long getPeriodeByDaysInMonths(int month){
        long periode;
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                periode = 2_592_000_000L;
                break;
            case 2:
                periode = (Calendrier.isBissextile(Calendrier.getCurrentYear())) ? 2_419_200_000L : 2_332_800_000L;
                break;

            default:
                periode = 2_505_600_000L;
                break;
        }

        return periode;
    }

    private int[] get6MonthsPeriod(){
        int[] values = new int[3];

        int month = Calendrier.getCurrentMonth();
        int actualDay = Calendrier.getCurrentDay();
        int year = Calendrier.getCurrentYear();
        int day = 0;

        for(int i = 0; i < 6; ++i){
            if(month++ == 12) {
                month = 1;
                year++;
            }
        }


        if(actualDay == 1) day = Calendrier.getNumberOfDaysInMonth(month);
        else day = actualDay -1;

        values[0] = day; values[1] = month; values[2] = year;

        return values;
    }
}
