package com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires;

import static com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler.*;

/**
 * Created by macbook on 28/08/17.
 */

public class TablesNames {

    public static final String getTableUTilisateurQueryCreateLite(){

        return "CREATE TABLE IF NOT EXISTS " + TABLE_UTILISATEUR + " (" +
                KEY + "  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "  +
                NOM + " TEXT NOT NULL, " +
                PRENOM + " TEXT NOT NULL, " +
                EMAIL + " TEXT NOT NULL,  " +
                NAISSANCE  + " DATETIME NOT NULL, "  +
                SUBSCRIBER_ID  + " TEXT NOT NULL, "  +
        NUMERO + " TEXT NOT NULL,  "  +
        MOT_DE_PASSE + " TEXT NOT NULL,  "  +
        VILLE + " TEXT NOT NULL,  "  +
        ID_CARD + " TEXT NOT NULL,  "  +
        TYPE_COMPTE + " TEXT NOT NULL, "  +
                SOLDE_COMPTE_LOCAL + " INTEGER NOT NULL,  " +
                SOLDE_COMPTE_LIGNE + " INTEGER NOT NULL,  " +
                SOLDE_COMPTE_EPARGNE_LOCAL + " INTEGER NOT NULL,  " +
                SOLDE_COMPTE_EPARGNE_LIGNE + " INTEGER NOT NULL,  " +
                EST_ABONNE + "  BOOLEAN NOT NULL,  " +
        HAS_NOTIFIED_PHONE_CHANGE + " BOOLEAN NOT NULL, " +
                DATE_INSCRIPTION + " DATETIME NOT NULL);";
    }


    public static final String getTableAbonnementQueryCreateLite(){

        return "CREATE TABLE IF NOT EXISTS " + TABLE_ABONNEMENT + " (" +
                KEY + "  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "  +
                ID_UTILISATEUR + " INTEGER NOT NULL, " +
                TYPE_ABONNEMENT + " TEXT NOT NULL, " +
                DATE_ABONNEMENT + " DATETIME NOT NULL,  " +
                MONTANT_ABONNEMENT  + " INTEGER NOT NULL, "  +
                DATE_FIN_ABONNEMENT + " DATETIME NOT NULL, " +
                EST_ACTIF + "  BOOLEAN NOT NULL);";
    }

    public static final String getTableCodesQueryCreateLite(){

        return "CREATE TABLE IF NOT EXISTS " + TABLE_CODE + " (" +
                KEY + "  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "  +
                ID_UTILISATEUR + " INTEGER NOT NULL, " +
                CODE + " TEXT NOT NULL, " +
                DATE_DEBUT + " DATETIME NOT NULL,  " +
                DATE_FIN + " DATETIME NOT NULL, " +
                EST_SAUVEGARDE + "  BOOLEAN NOT NULL);";
    }

    public static final String getTableParametresQueryCreateLite(){

        return "CREATE TABLE IF NOT EXISTS " + TABLE_PARAMETRES + " (" +
                KEY + "  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "  +
                SAUVEGARDE_INTERNET + " DATETIME NOT NULL, " +
                APP_FREEZED + "  BOOLEAN NOT NULL);";
    }

    /*public static final String getTableUTilisateurQueryCreate(){

        return "CREATE TABLE IF NOT EXISTS " + TABLE_UTILISATEUR + " (" +
                KEY + "  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT, "  +
                NOM + " VARCHAR(255) NOT NULL, " +
                PRENOM + " VARCHAR(255) NOT NULL, " +
                EMAIL + " VARCHAR(255) NOT NULL,  " +
                NAISSANCE  + " CHAR(10) NOT NULL, "  +
                NUMERO + " VARCHAR(255) NOT NULL,  "  +
                MOT_DE_PASSE + " VARCHAR(255) NOT NULL,  "  +
                VILLE + " VARCHAR(255) NOT NULL,  "  +
                ID_CARD + " VARCHAR(255) NOT NULL,  "  +
                TYPE_COMPTE + " VARCHAR(255) NOT NULL, "  +
                SOLDE_COMPTE_LOCAL + " BIGINT UNSIGNED NOT NULL,  " +
                SOLDE_COMPTE_LIGNE + " BIGINT UNSIGNED NOT NULL,  " +
                SOLDE_COMPTE_EPARGNE_LOCAL + " BIGINT UNSIGNED NOT NULL,  " +
                SOLDE_COMPTE_EPARGNE_LIGNE + " BIGINT UNSIGNED NOT NULL,  " +
                EST_ABONNE + "  TINYINT(1) UNSIGNED NOT NULL,  " +
                HAS_NOTIFIED_PHONE_CHANGE + " TINYINT(1) UNSIGNED NOT NULL, PRIMARY KEY (" + KEY + " ), UNIQUE " + NUMERO + " (" + NUMERO + "), UNIQUE " + ID_CARD + " (" + ID_CARD + ")) ENGINE = InnoDB;";
    }


    public static final String getTableAbonnementQueryCreate(){

        return "CREATE TABLE IF NOT EXISTS " + TABLE_ABONNEMENT + " (" +
                KEY + "  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT, "  +
                ID_UTILISATEUR + " BIGINT UNSIGNED NOT NULL, " +
                TYPE_ABONNEMENT + " VARCHAR(255) NOT NULL, " +
                DATE_ABONNEMENT + " CHAR(14) NOT NULL,  " +
                MONTANT_ABONNEMENT  + " INT UNSIGNED NOT NULL, "  +
                DATE_FIN_ABONNEMENT + " CHAR(14) NOT NULL,  " +
                "PRIMARY KEY (" + KEY + ") ) ENGINE = InnoDB;";
    }*/


}
