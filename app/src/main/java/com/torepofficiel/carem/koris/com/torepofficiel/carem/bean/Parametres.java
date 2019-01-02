package com.torepofficiel.carem.koris.com.torepofficiel.carem.bean;

import java.sql.Timestamp;

/**
 * Created by macbook on 13/09/17.
 */

public class Parametres {
    private Integer id;
    private Timestamp sauvegarde_internet;
    private boolean app_freezed;
    private boolean est_null;

    public Parametres(Timestamp sauvegardeDinternet, boolean app_freezed) {
        this.id = 1;
        this.sauvegarde_internet = sauvegardeDinternet;
        this.app_freezed = app_freezed;
        est_null = false;
    }

    public Parametres() {
        this.id = 1;
        this.sauvegarde_internet = new Timestamp(System.currentTimeMillis());
        app_freezed = false;
        est_null = false;
    }

    public Integer getId() {
        return id;
    }

    public Timestamp getSauvegardeDinternet() {
        return sauvegarde_internet;
    }

    public void setSauvegardeDinternet(Timestamp sauvegardeDinternet) {
        this.sauvegarde_internet = sauvegardeDinternet;
    }

    public boolean isEst_null() {
        return est_null;
    }

    public void setEst_null(boolean est_null) {
        this.est_null = est_null;
    }

    public boolean isApp_freezed() {
        return app_freezed;
    }

    public void setApp_freezed(boolean app_freezed) {
        this.app_freezed = app_freezed;
    }
}
