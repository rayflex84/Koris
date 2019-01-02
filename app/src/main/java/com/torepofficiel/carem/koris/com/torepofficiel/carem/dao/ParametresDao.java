package com.torepofficiel.carem.koris.com.torepofficiel.carem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Parametres;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DaoBase;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;

import java.sql.Timestamp;

import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.KEY;
import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.TABLE_PARAMETRES;

/**
 * Created by macbook on 13/09/17.
 */

public class ParametresDao extends DaoBase {


    public ParametresDao(Context context){
        super((ErrorActivity.currentActivity == null || ErrorActivity.currentActivity.get() == null)
                ? context : ErrorActivity.currentActivity.get());
    }

    /**
     * @param values le ContentValues pour modification à la base
     */
    public void modifier(ContentValues values) {

        open().update(TABLE_PARAMETRES, values, KEY + " = 1 ", new String[]{});
    }


    public Parametres selectionnerParametres() {

        Parametres parametres = new Parametres();

        String[] columns = new String[]{DataBaseHandler.SAUVEGARDE_INTERNET, DataBaseHandler.APP_FREEZED};

        Cursor c = open().query(TABLE_PARAMETRES, columns, DataBaseHandler.KEY + " = 1 ", new String[]{},null,null,null);

        if(c != null && c.moveToNext()){
            parametres = new Parametres(Timestamp.valueOf(c.getString(0)), c.getInt(1) > 0);
            c.close();
        } else {
            parametres.setEst_null(true);
        }

        return parametres;
    }


}
