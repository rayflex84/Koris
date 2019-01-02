package com.torepofficiel.carem.koris.com.torepofficiel.carem.bean;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.exceptions.ValeurChampsIncorrecteException;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.Calendrier;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.CryptageSha512;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by macbook on 26/08/17.
 */

public class Utilisateur implements Serializable {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Timestamp naissance;
    private String subscriber_id;
    private String numero;
    private String mot_de_passe;
    private String ville;
    private Long solde_compte_local = 0L;
    private Long solde_compte_ligne = 0L;
    private Long solde_compte_epargne_local = 0L;
    private Long solde_compte_epargne_ligne = 0L;
    private String type_compte;
    private Boolean est_abonne = false;
    private Boolean has_notified_phone_change = false;
    private String id_card;
    private Timestamp date_inscription;
    private boolean est_null = true;
    private Abonnement hisAbonnement = new Abonnement();
    private boolean app_freezed = false;
    private ArrayList<Code_Securite_Transactions> listeCode = new ArrayList<>();

    public Utilisateur(){
        super();
        est_null = true;
    }

    public Utilisateur(Long id, String nom, String prenoms, String email, Timestamp date_naissance,String subscriber_id, String numero,
                       String mot_de_passe, String ville, Long solde_compte_local, Long solde_compte_ligne,
                       Long solde_compte_epargne_local, Long solde_compte_epargne_ligne, String type_compte,
                       Boolean estAbonne, Boolean hasNotifiedPhoneChange, String idCard, Timestamp date_inscription) {

        this.id = id;
        this.nom = nom;
        this.prenom = prenoms;
        this.email = email;
        this.naissance = date_naissance;
        this.subscriber_id = subscriber_id;
        this.numero = numero;
        this.mot_de_passe = mot_de_passe;
        this.ville = ville;
        this.solde_compte_local = solde_compte_local;
        this.solde_compte_ligne = solde_compte_ligne;
        this.solde_compte_epargne_local = solde_compte_epargne_local;
        this.solde_compte_epargne_ligne = solde_compte_epargne_ligne;
        this.type_compte = type_compte;
        this.est_abonne = estAbonne;
        this.has_notified_phone_change = hasNotifiedPhoneChange;
        this.date_inscription = date_inscription;
        this.id_card = idCard;
        est_null = false;

    }

    public Utilisateur(String nom, String prenoms, String email, Timestamp date_naissance,String subscriber_id, String numero,
                       String mot_de_passe, String ville, Long solde_compte_local, Long solde_compte_ligne,
                       Long solde_compte_epargne_local, Long solde_compte_epargne_ligne, String type_compte,
                       Boolean estAbonne, Boolean hasNotifiedPhoneChange, String idCard, Timestamp date_inscription) {

        this.nom = nom;
        this.prenom = prenoms;
        this.email = email;
        this.naissance = date_naissance;
        this.subscriber_id = subscriber_id;
        this.numero = numero;
        this.mot_de_passe = mot_de_passe;
        this.ville = ville;
        this.solde_compte_local = solde_compte_local;
        this.solde_compte_ligne = solde_compte_ligne;
        this.solde_compte_epargne_local = solde_compte_epargne_local;
        this.solde_compte_epargne_ligne = solde_compte_epargne_ligne;
        this.type_compte = type_compte;
        this.est_abonne = estAbonne;
        this.has_notified_phone_change = hasNotifiedPhoneChange;
        this.id_card = idCard;
        this.date_inscription = date_inscription;
        est_null = false;
    }

    public Utilisateur(String nom, String prenoms, String email, Timestamp date_naissance, String subscriber_id, String numero, String idCard,
                       String mot_de_passe, String ville, String type_compte) {

        this.nom = nom;
        this.prenom = prenoms;
        this.email = email;
        this.naissance = date_naissance;
        this.subscriber_id = subscriber_id;
        this.numero = numero;
        this.setMot_de_passe(mot_de_passe);
        this.ville = ville;
        this.solde_compte_local = 0L;
        this.solde_compte_ligne = 0L;
        this.solde_compte_epargne_local = 0L;
        this.solde_compte_epargne_ligne = 0L;
        this.type_compte = type_compte;
        this.est_abonne = false;
        this.has_notified_phone_change = false;
        this.id_card = idCard;
        this.date_inscription = new Timestamp(System.currentTimeMillis());
        app_freezed = false;
        est_null = false;
    }

    public Long getId() {
        return id;
    }

    public Utilisateur setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Utilisateur setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getPrenom() {
        return prenom;
    }

    public Utilisateur setPrenom(String prenoms) {
        this.prenom = prenoms;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Utilisateur setEmail(String email) {
        this.email = email;
        return this;
    }

    public Timestamp getNaissance() {
        return naissance;
    }

    public Utilisateur setNaissance(Timestamp naissance) {
        this.naissance = naissance;
        return this;
    }

    public String getNumero() {
        return numero;
    }

    public Utilisateur setNumero(String numero) {
        this.numero = numero;
        return this;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public Utilisateur setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = CryptageSha512.crypt(mot_de_passe);
        return this;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Long getSolde_compte_local() {
        return solde_compte_local;
    }

    public void setSolde_compte_local(Long solde_compte_local) {
        this.solde_compte_local = solde_compte_local;
    }

    public Long getSolde_compte_ligne() {
        return solde_compte_ligne;
    }

    public void setSolde_compte_ligne(Long solde_compte_ligne) {
        this.solde_compte_ligne = solde_compte_ligne;
    }

    public Long getSolde_compte_epargne_local() {
        return solde_compte_epargne_local;
    }

    public void setSolde_compte_epargne_local(Long solde_compte_epargne_local) {
        this.solde_compte_epargne_local = solde_compte_epargne_local;
    }

    public Long getSolde_compte_epargne_ligne() {
        return solde_compte_epargne_ligne;
    }

    public void setSolde_compte_epargne_ligne(Long solde_compte_epargne_ligne) {
        this.solde_compte_epargne_ligne = solde_compte_epargne_ligne;
    }

    public String getType_compte() {
        return type_compte;
    }

    public void setType_compte(String type_compte) {
        this.type_compte = type_compte;
    }

    public Boolean getEst_abonne() {
        return est_abonne;
    }

    public void setEst_abonne(Boolean estAbonne) {
        this.est_abonne = estAbonne;
    }

    public Boolean getHas_notified_phone_change() {
        return has_notified_phone_change;
    }

    public void setHas_notified_phone_change(Boolean hasNotifiedPhoneChange) {
        this.has_notified_phone_change = hasNotifiedPhoneChange;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String idCard) {
        this.id_card= idCard;
    }

    public boolean isEst_null() {
        return est_null;
    }

    public void setEst_null(boolean est_null) {
        this.est_null = est_null;
    }

    public Abonnement getHisAbonnement() {
        return hisAbonnement;
    }

    public void setHisAbonnement(Abonnement hisAbonnement) {
        if(!hisAbonnement.isEst_null())
            this.hisAbonnement = hisAbonnement;
    }

    public Timestamp getDate_inscription() {
        return date_inscription;
    }

    public void setDate_inscription(Timestamp date_inscription) {
        this.date_inscription = date_inscription;
    }

    public String getSubscriber_id() {
        return subscriber_id;
    }

    public void setSubscriber_id(String subscriber_id) {
        this.subscriber_id = subscriber_id;
    }

    public ArrayList<Code_Securite_Transactions> getListeCode() {
        return listeCode;
    }

    public void release(){
        hisAbonnement = null;
        listeCode = null;
    }

    public void setListeCode(ArrayList<Code_Securite_Transactions> listeCode) {
        this.listeCode = listeCode;
    }

    public void addListeCode(Code_Securite_Transactions code){
        this.listeCode.add(code);
    }

    public boolean isApp_freezed() {
        return app_freezed;
    }

    public void setApp_freezed(boolean app_freezed) {
        this.app_freezed = app_freezed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;

        return numero.equals(that.numero);

    }

    @Override
    public int hashCode() {
        return numero.hashCode();
    }

    @Override
    public String toString() {

        String[] dateSplitted = naissance.toString().split("-");
        short year = Short.parseShort(dateSplitted[0]);
        byte month = Byte.parseByte(dateSplitted[1]);
        byte day = Byte.parseByte(dateSplitted[2].split(" ")[0]);


        Calendrier calendrier=null;
        try {
            calendrier = new Calendrier(day, month,
                    year);
        } catch (ValeurChampsIncorrecteException e) {
            e.printStackTrace();
        }
        return nom + " " + prenom + ", " + email + ", né "+ calendrier.toString() + ", "
                + subscriber_id + ", "+ numero + ", " + mot_de_passe + ", " + id_card + ", "
                + ville + ", "+ solde_compte_epargne_ligne + ", "
                + has_notified_phone_change + ", "+ type_compte + ((est_abonne) ? "est " : " n'est pas ") + "abonné(e)";
    }
}
