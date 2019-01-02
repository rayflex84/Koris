package com.torepofficiel.carem.koris;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.custom_volley.CustomJSonObjectRequest;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabFragment1 extends Fragment {

    private static WeakReference<TabFragment1> tabFragment1;
    public RequestQueue mRequestQueue;

    TextView solde_epargne_tab1,
            solde_ligne_tab1;

    public static final String CANCEL_TAG = "cancel_tag";

    RelativeLayout solde_online_progressbar;
    ProgressBar mProgressBar;
    RelativeLayout connexion_impossible_block;
    TableLayout fragment1_online_block;

    public TabFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tabFragment1 = new WeakReference<>(this);

        View rootView = inflater.inflate(R.layout.fragment_koris1, container, false);
        solde_ligne_tab1 = (TextView) rootView.findViewById(R.id.section_label_1);
        RelativeLayout linear_content_tab1 = (RelativeLayout) rootView.findViewById(R.id.linear_content_1);
        linear_content_tab1.setBackgroundResource(R.drawable.custom_solde_background);

        solde_epargne_tab1 = (TextView) rootView.findViewById(R.id.section_label1_1);

        Button but_cpt_local_1 = (Button) rootView.findViewById(R.id.but_cpt_local_1);
        Button but_cpt_ligne_1 = (Button) rootView.findViewById(R.id.but_cpt_ligne_1);
        Button but_cpt_epargne_ligne = (Button) rootView.findViewById(R.id.but_cpt_epargne_ligne);
        setRoundedButtonBackground(but_cpt_local_1, but_cpt_ligne_1, but_cpt_epargne_ligne);

        solde_online_progressbar = rootView.findViewById(R.id.solde_online_progress);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(KorisActivity.getInstance(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        connexion_impossible_block = rootView.findViewById(R.id.connexion_impossible_block);
        fragment1_online_block = rootView.findViewById(R.id.fragment1_online_block);

        attachIntanceToRequestQueue();

        but_cpt_ligne_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KorisActivity.getInstance(), KorisCrediterCompteActivity.class);
                intent.putExtra(KorisActivity.WHICH_FRAGMENT, 1);
                KorisActivity.getInstance().startActivityForResult(intent, KorisActivity.REQUESTCODE);
            }
        });

        but_cpt_local_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KorisActivity.getInstance(), KorisTransfererCompteActivity.class);
                intent.putExtra(KorisActivity.WHICH_FRAGMENT, 0);
                KorisActivity.getInstance().startActivityForResult(intent, KorisActivity.REQUESTCODE);
            }
        });

        but_cpt_epargne_ligne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KorisActivity.getInstance(), KorisTransfererCompteEpargneActivity.class);
                intent.putExtra(KorisActivity.WHICH_FRAGMENT, 1);
                KorisActivity.getInstance().startActivityForResult(intent, KorisActivity.REQUESTCODE);
            }
        });

        Button but_impossible_connexion = rootView.findViewById(R.id.but_impossible_connexion);

        but_impossible_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataOnline();
            }
        });


        return rootView;
    }

    public void switchViews(int how){
        switch (how){
            case 0:
                connexion_impossible_block.setVisibility(View.GONE);
                fragment1_online_block.setVisibility(View.VISIBLE);
                solde_online_progressbar.setVisibility(View.GONE);
                break;

            case 1:
                connexion_impossible_block.setVisibility(View.VISIBLE);
                fragment1_online_block.setVisibility(View.GONE);
                solde_online_progressbar.setVisibility(View.GONE);
                break;

            case 2:
                connexion_impossible_block.setVisibility(View.GONE);
                fragment1_online_block.setVisibility(View.GONE);
                solde_online_progressbar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void fetchDataOnline(){

        switchViews(2);
        if(mRequestQueue != null){
            mRequestQueue.start();
        }

        String url = "https://torepofficiel.com/web/main_controlleur.php/koris/fragment/solde/compte/online/" + ClasseUtilitaire.getUserSubscriberId();

        final CustomJSonObjectRequest jsonObjectRequest = new CustomJSonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mRequestQueue.stop();

                        try {
                            if (response.getInt("success") == 1){

                                solde_ligne_tab1.setText(ClasseUtilitaire.numberFormatted(response.getInt("solde_compte_ligne")));
                                solde_epargne_tab1.setText( getString(R.string.string_solde_compte_epargne, ClasseUtilitaire.numberFormatted(response.getInt("solde_epargne_ligne")) ));
                                switchViews(0);

                            } else {
                                switchViews(1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            switchViews(1);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRequestQueue.stop();

                switchViews(1);

            }
        });

        jsonObjectRequest.setTag(CANCEL_TAG);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRequestQueue.add(jsonObjectRequest);
            }
        }, 1500);
    }

    public void attachIntanceToRequestQueue(){
        mRequestQueue = Volley.newRequestQueue(KorisActivity.getInstance().getApplicationContext());
    }

    private void setRoundedButtonBackground(Button... buttons) {
        for (Button button : buttons)
            button.setBackgroundResource(R.drawable.custom_solde_background);
    }

    public static TabFragment1 getInstance() {
        return (tabFragment1 != null) ? tabFragment1.get() : null;
    }
}
