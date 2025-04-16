package com.formation.centre.dto;

public class CreditDTO {
    private String id;
    private String proprieteId;
    private String banque;
    private String montantEmprunte;
    private String dateDebut;
    private String dateFin;
    private String dureeMois;
    private String tauxInteretAnnuel;
    private String mensualite;
    private String assuranceMensuelle;
    private String fraisDossier;
    private String fraisGarantie;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(String proprieteId) {
        this.proprieteId = proprieteId;
    }

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public String getMontantEmprunte() {
        return montantEmprunte;
    }

    public void setMontantEmprunte(String montantEmprunte) {
        this.montantEmprunte = montantEmprunte;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(String dureeMois) {
        this.dureeMois = dureeMois;
    }

    public String getTauxInteretAnnuel() {
        return tauxInteretAnnuel;
    }

    public void setTauxInteretAnnuel(String tauxInteretAnnuel) {
        this.tauxInteretAnnuel = tauxInteretAnnuel;
    }

    public String getMensualite() {
        return mensualite;
    }

    public void setMensualite(String mensualite) {
        this.mensualite = mensualite;
    }

    public String getAssuranceMensuelle() {
        return assuranceMensuelle;
    }

    public void setAssuranceMensuelle(String assuranceMensuelle) {
        this.assuranceMensuelle = assuranceMensuelle;
    }

    public String getFraisDossier() {
        return fraisDossier;
    }

    public void setFraisDossier(String fraisDossier) {
        this.fraisDossier = fraisDossier;
    }

    public String getFraisGarantie() {
        return fraisGarantie;
    }

    public void setFraisGarantie(String fraisGarantie) {
        this.fraisGarantie = fraisGarantie;
    }
}