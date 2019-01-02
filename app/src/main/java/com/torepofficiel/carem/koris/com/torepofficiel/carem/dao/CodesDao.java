package com.torepofficiel.carem.koris.com.torepofficiel.carem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Code_Securite_Transactions;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DaoBase;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;

import java.sql.Timestamp;
import java.util.ArrayList;

import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.TABLE_CODE;

/**
 * Created by macbook on 12/09/17.
 */

public class CodesDao extends DaoBase {

    public CodesDao(Context context){
        super((ErrorActivity.currentActivity == null || ErrorActivity.currentActivity.get() == null)
                ? context : ErrorActivity.currentActivity.get());
    }

    /**
     * @param c le Code à ajouter à la base
     */
    public long ajouter(Code_Securite_Transactions c) {
        ContentValues value = new ContentValues();
        value.put(DataBaseHandler.CODE, c.getCode());
        value.put(DataBaseHandler.ID_UTILISATEUR, c.getUtilisateur_id());
        value.put(DataBaseHandler.DATE_DEBUT, c.getDate_debut().toString());
        value.put(DataBaseHandler.DATE_FIN, c.getDate_fin().toString());
        value.put(DataBaseHandler.EST_SAUVEGARDE, c.isEst_sauvegarde());

        return open().insert(TABLE_CODE, null, value);
    }

    /**
     * @param id_utilisateur l'identifiant de l'abonnement à récupérer
     */
    public ArrayList<Code_Securite_Transactions> selectionnerTousLesCodes(long id_utilisateur) {

        ArrayList<Code_Securite_Transactions> results = new ArrayList<>();
        ArrayList<Long> toBeDeleted = new ArrayList<>();

        String[] columns = new String[]{DataBaseHandler.KEY, DataBaseHandler.CODE, DataBaseHandler.DATE_DEBUT, DataBaseHandler.DATE_FIN,
                DataBaseHandler.EST_SAUVEGARDE};

        Cursor c = open().query(TABLE_CODE, columns, DataBaseHandler.ID_UTILISATEUR + " = ? ", new String[]{String.valueOf(id_utilisateur)},null,null,null);

        if(c != null){
            while (c.moveToNext()){

                Timestamp date_fin = Timestamp.valueOf(c.getString(3));
                Timestamp temp = new Timestamp(date_fin.getTime() + 1_800_000L);
                Timestamp now = new Timestamp( System.currentTimeMillis() );
                if(now.compareTo( temp ) < 0 ) {
                    toBeDeleted.add(c.getLong(0));
                } else {
                    boolean est_sauvegarde = c.getInt(4) > 0;
                    results.add(new Code_Securite_Transactions(c.getString(1), id_utilisateur, Timestamp.valueOf(c.getString(2)), date_fin, est_sauvegarde));
                }
            }

            c.close();
        }

        if(toBeDeleted.size() > 0){
            for(Long id : toBeDeleted){
                supprimer(id);
            }
        }

        return results;
    }

    /**
     * @param id l'identifiant du métier à supprimer
     */
    public void supprimer(long id) {
        open().delete(TABLE_CODE, DataBaseHandler.KEY + " = ?", new String[] {String.valueOf(id)});
    }

}
