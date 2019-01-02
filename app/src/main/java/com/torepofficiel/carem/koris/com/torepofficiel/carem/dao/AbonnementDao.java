package com.torepofficiel.carem.koris.com.torepofficiel.carem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Abonnement;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DaoBase;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;

import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;

import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.ID_UTILISATEUR;
import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.KEY;
import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.TABLE_ABONNEMENT;

/**
 * Created by macbook on 28/08/17.
 */

public class AbonnementDao extends DaoBase {

    private WeakReference<Context> mContext;

    public AbonnementDao( Context context){
        super((ErrorActivity.currentActivity == null || ErrorActivity.currentActivity.get() == null)
                ? context : ErrorActivity.currentActivity.get());
        mContext = new WeakReference<Context>((ErrorActivity.currentActivity == null || ErrorActivity.currentActivity.get() == null)
                ? context : ErrorActivity.currentActivity.get());
    }

    /**
     * @param a l'abonnement à ajouter à la base
     */
    public long ajouter(Abonnement a) {
        ContentValues value = new ContentValues();
        value.put(DataBaseHandler.MONTANT_ABONNEMENT, a.getMontant_abonnement());
        value.put(DataBaseHandler.TYPE_ABONNEMENT, a.getType_abonnement());
        value.put(DataBaseHandler.DATE_ABONNEMENT, a.getDate_abonnement().toString());
        value.put(DataBaseHandler.DATE_FIN_ABONNEMENT, a.getDate_fin_abonnement().toString());
        value.put(DataBaseHandler.ID_UTILISATEUR, a.getUtilisateur_id());
        value.put(DataBaseHandler.EST_ACTIF, a.isEst_actif());

        return open().insert(TABLE_ABONNEMENT, null, value);
    }

    /**
     * @param id_abonnement l'identifiant de l'abonnement à récupérer
     */
    public Abonnement selectionner(long id_abonnement)throws InterruptedException, ExecutionException {
        Abonnement abonnement = new Abonnement();

        String[] columns = new String[]{DataBaseHandler.TYPE_ABONNEMENT, DataBaseHandler.DATE_ABONNEMENT, DataBaseHandler.MONTANT_ABONNEMENT,
                DataBaseHandler.DATE_FIN_ABONNEMENT, DataBaseHandler.ID_UTILISATEUR, DataBaseHandler.EST_ACTIF};

        Cursor c = open().query(TABLE_ABONNEMENT, columns, DataBaseHandler.KEY + " = ? ", new String[]{String.valueOf(id_abonnement)},null,null,null);
        if(c != null && c.moveToFirst()){

            abonnement = new Abonnement(id_abonnement, c.getString(0), Timestamp.valueOf(c.getString(1)), c.getLong(2), Timestamp.valueOf(c.getString(3)), 1L, c.getInt(5) > 0);

            c.close();
        }

        return abonnement;
    }

    /**
     *
     * @param utilisateur_id the id of Utilisateur
     * @return a cursor containing all Abonnements of one Utilisateur
     */
    public Cursor getCursorCurrentAbonnementsOfAnUtilisateur(Long utilisateur_id) {

        String[] columns = new String[]{DataBaseHandler.KEY, DataBaseHandler.TYPE_ABONNEMENT, DataBaseHandler.DATE_ABONNEMENT, DataBaseHandler.MONTANT_ABONNEMENT,
                DataBaseHandler.DATE_FIN_ABONNEMENT, DataBaseHandler.EST_ACTIF};

        return open().query(TABLE_ABONNEMENT, columns, ID_UTILISATEUR + " = ?", new String[]{String.valueOf(utilisateur_id)}, null,null,null,null);


    }

    public Abonnement getCurrentAbonnementsOfAnUtilisateur(Long utilisateur_id){
        Abonnement abonnement = new Abonnement();

        Cursor cursor = getCursorCurrentAbonnementsOfAnUtilisateur(utilisateur_id);
        if (cursor != null && cursor.moveToNext()){

            abonnement = new Abonnement(cursor.getLong(0), cursor.getString(1), Timestamp.valueOf(cursor.getString(2)),
                    cursor.getLong(3), Timestamp.valueOf(cursor.getString(4)), utilisateur_id, cursor.getInt(5) > 0);

            cursor.close();
        }

        return abonnement;
    }

    /**
     * @param values le ContentValues pour modification à la base
     */
    public void modifier(ContentValues values) {

        open().update(TABLE_ABONNEMENT, values, KEY + " = 1 ", new String[]{});
    }

    /**
     * @param id l'identifiant du métier à supprimer
     */
    public void supprimer(long id) {
        open().delete(TABLE_ABONNEMENT, DataBaseHandler.KEY + " = ?", new String[] {String.valueOf(id)});
    }
}
