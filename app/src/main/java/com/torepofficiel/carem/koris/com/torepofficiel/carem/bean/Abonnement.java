package com.torepofficiel.carem.koris.com.torepofficiel.carem.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by macbook on 28/08/17.
 */

public class Abonnement implements Serializable {

    private Long id;
    private String type_abonnement;
    private Timestamp date_abonnement;
    private Long montant_abonnement;
    private Timestamp date_fin_abonnement;
    private Long utilisateur_id;
    private boolean est_null = true;
    private boolean est_actif = true;

    public Abonnement(){
        super();
        est_null = true;
        this.utilisateur_id = 1L;
        this.type_abonnement = "Une journée";
        this.date_abonnement = new Timestamp(System.currentTimeMillis());
        this.montant_abonnement = 200L;
        this.date_fin_abonnement = new Timestamp(System.currentTimeMillis());;
        this.est_actif = false;

    }

    public Abonnement(Long id, String type_abonnement, Timestamp date_abonnement, Long montant_abonnement,
                      Timestamp date_fin_abonnement, Long utilisateur_id, boolean est_actif) {
        this.id = id;
        this.type_abonnement = type_abonnement;
        this.date_abonnement = date_abonnement;
        this.montant_abonnement = montant_abonnement;
        this.date_fin_abonnement = date_fin_abonnement;
        this.utilisateur_id = utilisateur_id;
        est_null = false;
        this.est_actif = est_actif;
    }

    public Abonnement( String type_abonnement, Timestamp date_abonnement, Long montant_abonnement,
                      Timestamp date_fin_abonnement, Long utilisateur, boolean est_actif) {
        this.type_abonnement = type_abonnement;
        this.date_abonnement = date_abonnement;
        this.montant_abonnement = montant_abonnement;
        this.date_fin_abonnement = date_fin_abonnement;
        this.utilisateur_id = utilisateur;
        est_null = false;
        this.est_actif = est_actif;
    }

    public Abonnement( String type_abonnement, Timestamp date_abonnement, Long montant_abonnement,
                      Timestamp date_fin_abonnement, Long utilisateur) {
        this.type_abonnement = type_abonnement;
        this.date_abonnement = date_abonnement;
        this.montant_abonnement = montant_abonnement;
        this.date_fin_abonnement = date_fin_abonnement;
        this.utilisateur_id = utilisateur;
        est_null = false;
        est_actif = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType_abonnement() {
        return type_abonnement;
    }

    public void setType_abonnement(String type_abonnement) {
        this.type_abonnement = type_abonnement;
    }

    public Timestamp getDate_abonnement() {
        return date_abonnement;
    }

    public void setDate_abonnement(Timestamp date_abonnement) {
        this.date_abonnement = date_abonnement;
    }

    public Long getMontant_abonnement() {
        return montant_abonnement;
    }

    public void setMontant_abonnement(Long montant_abonnement) {
        this.montant_abonnement = montant_abonnement;
    }

    public Timestamp getDate_fin_abonnement() {
        return date_fin_abonnement;
    }

    public void setDate_fin_abonnement(Timestamp date_fin_abonnement) {
        this.date_fin_abonnement = date_fin_abonnement;
    }

    public Long getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur(Long utilisateur) {
        this.utilisateur_id = utilisateur;
    }

    public boolean isEst_null() {
        return est_null;
    }

    public Abonnement setEst_null(boolean est_null) {
        this.est_null = est_null;
        return this;
    }

    public boolean isEst_actif() {
        return est_actif;
    }

    public void setEst_actif(boolean est_actif) {
        this.est_actif = est_actif;
    }

}
