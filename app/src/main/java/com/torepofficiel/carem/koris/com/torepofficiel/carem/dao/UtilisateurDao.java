package com.torepofficiel.carem.koris.com.torepofficiel.carem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DaoBase;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;

import java.sql.Timestamp;

import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.KEY;
import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.TABLE_UTILISATEUR;

/**
 * Created by macbook on 25/08/17.
 */

public class UtilisateurDao extends DaoBase {

    public UtilisateurDao(Context context){
        super((ErrorActivity.currentActivity == null || ErrorActivity.currentActivity.get() == null)
        ? context : ErrorActivity.currentActivity.get());
    }

    /**
     * @param u l'Utilisateur à ajouter à la base
     */
    public long ajouter(Utilisateur u) {
        ContentValues value = new ContentValues();
        value.put(DataBaseHandler.NOM, u.getNom());
        value.put(DataBaseHandler.PRENOM, u.getPrenom());
        value.put(DataBaseHandler.EMAIL, u.getEmail());
        value.put(DataBaseHandler.NUMERO, u.getNumero());
        value.put(DataBaseHandler.MOT_DE_PASSE, u.getMot_de_passe());
        value.put(DataBaseHandler.NAISSANCE, u.getNaissance().toString());
        value.put(DataBaseHandler.SUBSCRIBER_ID, u.getSubscriber_id());
        value.put(DataBaseHandler.VILLE, u.getVille());
        value.put(DataBaseHandler.SOLDE_COMPTE_LOCAL, u.getSolde_compte_local());
        value.put(DataBaseHandler.SOLDE_COMPTE_LIGNE, u.getSolde_compte_ligne());
        value.put(DataBaseHandler.SOLDE_COMPTE_EPARGNE_LOCAL, u.getSolde_compte_epargne_local());
        value.put(DataBaseHandler.SOLDE_COMPTE_EPARGNE_LIGNE, u.getSolde_compte_epargne_ligne());
        value.put(DataBaseHandler.TYPE_COMPTE, u.getType_compte());
        value.put(DataBaseHandler.EST_ABONNE, u.getEst_abonne());
        value.put(DataBaseHandler.HAS_NOTIFIED_PHONE_CHANGE, u.getHas_notified_phone_change());
        value.put(DataBaseHandler.ID_CARD, u.getId_card());
        value.put(DataBaseHandler.DATE_INSCRIPTION, u.getDate_inscription().toString());

        return open().insert(TABLE_UTILISATEUR, null, value);
    }

    /**
     * @param id l'identifiant du métier à récupérer
     */
    public Utilisateur selectionner(long id) {

        Utilisateur utilisateur = new Utilisateur();

        String[] columns = new String[]{DataBaseHandler.NOM, DataBaseHandler.PRENOM, DataBaseHandler.EMAIL,
                DataBaseHandler.NAISSANCE, DataBaseHandler.SUBSCRIBER_ID, DataBaseHandler.NUMERO, DataBaseHandler.MOT_DE_PASSE,
                DataBaseHandler.VILLE, DataBaseHandler.SOLDE_COMPTE_LOCAL, DataBaseHandler.SOLDE_COMPTE_LIGNE,
                DataBaseHandler.SOLDE_COMPTE_EPARGNE_LOCAL, DataBaseHandler.SOLDE_COMPTE_EPARGNE_LIGNE,
                DataBaseHandler.TYPE_COMPTE, DataBaseHandler.EST_ABONNE,
                DataBaseHandler.HAS_NOTIFIED_PHONE_CHANGE,
                DataBaseHandler.ID_CARD, DataBaseHandler.DATE_INSCRIPTION};

        Cursor c = open().query(TABLE_UTILISATEUR, columns, DataBaseHandler.KEY + " = ? ", new String[]{String.valueOf(id)},null,null,null);

        if(c != null && c.moveToFirst()) {
            boolean est_abonne = c.getInt(13) > 0;
            boolean has_notified_phone_change = c.getInt(14) > 0;

            utilisateur = new Utilisateur(id, c.getString(0), c.getString(1), c.getString(2), Timestamp.valueOf(c.getString(3)), c.getString(4),
                    c.getString(5), c.getString(6), c.getString(7), c.getLong(8), c.getLong(9),
                    c.getLong(10), c.getLong(11), c.getString(12), est_abonne,
                    has_notified_phone_change, c.getString(15), Timestamp.valueOf(c.getString(16)));

            c.close();
        }


        return utilisateur;
    }

    /*public Cursor getCursorOfAllUtilisateurs() {

        String[] columns = new String[]{DataBaseHandler.KEY, DataBaseHandler.NOM, DataBaseHandler.PRENOM, DataBaseHandler.EMAIL,
                DataBaseHandler.NAISSANCE, DataBaseHandler.SUBSCRIBER_ID, DataBaseHandler.NUMERO, DataBaseHandler.MOT_DE_PASSE,
                DataBaseHandler.VILLE, DataBaseHandler.SOLDE_COMPTE_LOCAL, DataBaseHandler.SOLDE_COMPTE_LIGNE,
                DataBaseHandler.SOLDE_COMPTE_EPARGNE_LOCAL, DataBaseHandler.SOLDE_COMPTE_EPARGNE_LIGNE,
                DataBaseHandler.TYPE_COMPTE, DataBaseHandler.EST_ABONNE,
                DataBaseHandler.HAS_NOTIFIED_PHONE_CHANGE,
                DataBaseHandler.ID_CARD, DataBaseHandler.DATE_INSCRIPTION};

        return open().query(TABLE_UTILISATEUR, columns, null, null,null,null,null);

    }

    public ArrayList<Utilisateur> getAllUtilisateurs(){

        ArrayList<Utilisateur> listOfAllUtilisateurs = new ArrayList<>();

        Cursor c = getCursorOfAllUtilisateurs();
        while (c != null && c.moveToNext()){

            boolean est_abonne = c.getInt(14) > 0;
            boolean has_notified_phone_change = c.getInt(15) > 0;

            Utilisateur utilisateur = new Utilisateur(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), Timestamp.valueOf(c.getString(4)),
                    c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getLong(9), c.getLong(10),
                    c.getLong(11), c.getLong(12), c.getString(13), est_abonne,
                    has_notified_phone_change, c.getString(16), Timestamp.valueOf(c.getString(17)));

            listOfAllUtilisateurs.add(utilisateur);
        }
        if(c != null) c.close();

        return listOfAllUtilisateurs;
    }*/

    /**
     * @param contentNewValues the ContentValues containing the new values
     */
    public void modifier(ContentValues contentNewValues){
        open().update(TABLE_UTILISATEUR, contentNewValues, KEY + " = 1", new String[]{});
    }

    /**
     * @param id l'identifiant du métier à supprimer
     */
    public void supprimer(long id) {
        open().delete(TABLE_UTILISATEUR, DataBaseHandler.KEY + " = ?", new String[] {String.valueOf(id)});
    }

}
