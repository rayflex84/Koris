package com.torepofficiel.carem.koris.com.torepofficiel.carem.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by macbook on 5/09/17.
 */

public class Code_Securite_Transactions implements Serializable {

    private Long id;
    private String code;
    private Long utilisateur_id;
    private Timestamp date_debut;
    private Timestamp date_fin;
    private boolean est_sauvegarde;

    public Code_Securite_Transactions(Long id, String code, Long utilisateur_id, Timestamp date_debut, Timestamp date_fin, boolean est_sauvegarde) {
        this.id = id;
        this.code = code;
        this.utilisateur_id = utilisateur_id;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.est_sauvegarde = est_sauvegarde;
    }

    public Code_Securite_Transactions(String code, Long utilisateur_id, Timestamp date_debut, Timestamp date_fin, boolean est_sauvegarde) {
        this.code = code;
        this.utilisateur_id = utilisateur_id;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.est_sauvegarde = est_sauvegarde;
    }

    public Code_Securite_Transactions(String code, Long utilisateur_id) {
        this.code = code;
        this.utilisateur_id = utilisateur_id;
        this.date_debut = new Timestamp(System.currentTimeMillis());
        this.date_fin = new Timestamp(this.date_debut.getTime() + 1_800_000);
        this.est_sauvegarde = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(Long utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public Timestamp getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Timestamp date_debut) {
        this.date_debut = date_debut;
    }

    public Timestamp getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Timestamp date_fin) {
        this.date_fin = date_fin;
    }

    public boolean isEst_sauvegarde() {
        return est_sauvegarde;
    }

    public void setEst_sauvegarde(boolean est_sauvegarde) {
        this.est_sauvegarde = est_sauvegarde;
    }
}
