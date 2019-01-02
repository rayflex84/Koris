package com.torepofficiel.carem.koris.com.torepofficiel.carem.metier;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.TablesNames;

import java.sql.Timestamp;

/**
 * Created by macbook on 25/08/17.
 */

public class DataBaseHandler extends SQLiteOpenHelper {


    protected final static int VERSION = 1;
    // Le nom du fichier qui représente ma base
    protected final static String DATABASE_NAME = "koris.db";

    //Données Utilisateur
    public static final String TABLE_UTILISATEUR = "utilisateur";
    public static final String KEY = "_id";
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String EMAIL = "email";
    public static final String NAISSANCE =  "date_naissance";
    public static final String SUBSCRIBER_ID = "subscriber_id";
    public static final String NUMERO =  "numero";
    public static final String MOT_DE_PASSE =  "mot_de_passe";
    public static final String VILLE =  "ville";
    public static final String SOLDE_COMPTE_LOCAL = "solde_compte_local";
    public static final String SOLDE_COMPTE_LIGNE = "solde_compte_ligne";
    public static final String SOLDE_COMPTE_EPARGNE_LOCAL = "solde_compte_epargne_locale";
    public static final String SOLDE_COMPTE_EPARGNE_LIGNE = "solde_compte_epargne_ligne";
    public static final String TYPE_COMPTE = "type_compte";
    public static final String EST_ABONNE =  "est_abonne";
    public static final String HAS_NOTIFIED_PHONE_CHANGE = "has_notified_phone_change";
    public static final String ID_CARD = "id_card";
    public static final String DATE_INSCRIPTION = "date_inscription";

    //Données Abonnement
    public static final String TABLE_ABONNEMENT = "abonnement";
    public static final String ID_UTILISATEUR = "id_utilisateur";
    public static final String TYPE_ABONNEMENT = "type_abonnement";
    public static final String DATE_ABONNEMENT = "date_abonnement";
    public static final String MONTANT_ABONNEMENT = "montant_abonnement";
    public static final String DATE_FIN_ABONNEMENT ="date_fin_abonnement";
    public static final String EST_ACTIF ="est_actif";

    //Données Code_Securite_Transactions
    public static final String TABLE_CODE = "codes";
    public static final String CODE = "code";
    public static final String DATE_DEBUT = "date_dbut";
    public static final String DATE_FIN ="date_fin";
    public static final String EST_SAUVEGARDE = "est_sauvegarde";

    //Données Parametres
    public static final String TABLE_PARAMETRES = "parametres";
    public static final String SAUVEGARDE_INTERNET = "sauvegarde_internet";
    public static final String APP_FREEZED = "app_freezed";



    public static String METIER_ALTER_TABLE;

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TablesNames.getTableUTilisateurQueryCreateLite());
        sqLiteDatabase.execSQL(TablesNames.getTableAbonnementQueryCreateLite());
        sqLiteDatabase.execSQL(TablesNames.getTableCodesQueryCreateLite());
        sqLiteDatabase.execSQL(TablesNames.getTableParametresQueryCreateLite());

        ContentValues value = new ContentValues();
        value.put(DataBaseHandler.SAUVEGARDE_INTERNET, new Timestamp(504755907173L).toString());
        value.put(DataBaseHandler.APP_FREEZED, false);

        sqLiteDatabase.insert(TABLE_PARAMETRES, null, value);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(METIER_ALTER_TABLE);
    }
}
