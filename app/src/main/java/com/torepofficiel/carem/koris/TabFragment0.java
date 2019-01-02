package com.torepofficiel.carem.koris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabFragment0 extends Fragment {


    public TabFragment0() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_koris0, container, false);
        TextView solde_local_tab0 = (TextView) rootView.findViewById(R.id.section_label);
        RelativeLayout linear_content_tab0 = (RelativeLayout) rootView.findViewById(R.id.linear_content);
        linear_content_tab0.setBackgroundResource(R.drawable.custom_solde_background);

        Button but_cpt_local = (Button) rootView.findViewById(R.id.but_cpt_local);
        Button but_cpt_ligne = (Button) rootView.findViewById(R.id.but_cpt_ligne);
        Button but_cpt_epargne_local = (Button) rootView.findViewById(R.id.but_cpt_epargne_local);
        setRoundedButtonBackground(but_cpt_local, but_cpt_ligne, but_cpt_epargne_local);

        but_cpt_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KorisActivity.getInstance(), KorisCrediterCompteActivity.class);
                intent.putExtra(KorisActivity.WHICH_FRAGMENT, 0);
                KorisActivity.getInstance().startActivityForResult(intent, KorisActivity.REQUESTCODE);
            }
        });

        but_cpt_ligne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KorisActivity.getInstance(), KorisTransfererCompteActivity.class);
                intent.putExtra(KorisActivity.WHICH_FRAGMENT, 1);
                KorisActivity.getInstance().startActivityForResult(intent, KorisActivity.REQUESTCODE);
            }
        });

        but_cpt_epargne_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KorisActivity.getInstance(), KorisTransfererCompteEpargneActivity.class);
                intent.putExtra(KorisActivity.WHICH_FRAGMENT, 0);
                KorisActivity.getInstance().startActivityForResult(intent, KorisActivity.REQUESTCODE);
            }
        });


        TextView solde_epargne_tab0 = (TextView) rootView.findViewById(R.id.section_label1);

        solde_local_tab0.setText(ClasseUtilitaire.numberFormatted(KorisActivity.utilisateur_bundle.getLong(KorisActivity.SOLDE_LOCAL)));
        solde_epargne_tab0.setText(getString(R.string.string_solde_compte_epargne, ClasseUtilitaire.numberFormatted(KorisActivity.utilisateur_bundle.getLong(KorisActivity.SOLDE_EPARGNE_LOCAL))));


        return rootView;
    }

    private void setRoundedButtonBackground(Button... buttons) {
        for (Button button : buttons)
            button.setBackgroundResource(R.drawable.custom_solde_background);
    }
}
