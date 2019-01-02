package com.torepofficiel.carem.koris.com.torepofficiel.carem.metier;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.NOM;
import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.VERSION;

/**
 * Created by macbook on 25/08/17.
 */


public abstract class DaoBase {

    // Nous sommes à la première version de la base
// Si je décide de la mettre à jour, il faudra changer cet attribut
    protected SQLiteDatabase mDb = null;
    protected DataBaseHandler mHandler = null;

    public DaoBase(Context pContext) {
        this.mHandler = new DataBaseHandler(pContext, NOM, null, VERSION);
    }

    public DaoBase(Context pContext, String nomBase, SQLiteDatabase.CursorFactory factory, int version) {
        this.mHandler = new DataBaseHandler(pContext, nomBase, factory, version);
    }

    public SQLiteDatabase open() {
// Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        if(mDb != null)
            mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

}
