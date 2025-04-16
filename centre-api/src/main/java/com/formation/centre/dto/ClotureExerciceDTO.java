package com.formation.centre.dto;

public class ClotureExerciceDTO {
    private String id;
    private String utilisateurId;
    private String anneeFiscale;
    private String totalLoyers;
    private String totalCharges;
    private String totalAmortissements;
    private String resultatComptable;
    private String revenuImposable;
    private String dateCloture;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getAnneeFiscale() {
        return anneeFiscale;
    }

    public void setAnneeFiscale(String anneeFiscale) {
        this.anneeFiscale = anneeFiscale;
    }

    public String getTotalLoyers() {
        return totalLoyers;
    }

    public void setTotalLoyers(String totalLoyers) {
        this.totalLoyers = totalLoyers;
    }

    public String getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    public String getTotalAmortissements() {
        return totalAmortissements;
    }

    public void setTotalAmortissements(String totalAmortissements) {
        this.totalAmortissements = totalAmortissements;
    }

    public String getResultatComptable() {
        return resultatComptable;
    }

    public void setResultatComptable(String resultatComptable) {
        this.resultatComptable = resultatComptable;
    }

    public String getRevenuImposable() {
        return revenuImposable;
    }

    public void setRevenuImposable(String revenuImposable) {
        this.revenuImposable = revenuImposable;
    }

    public String getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(String dateCloture) {
        this.dateCloture = dateCloture;
    }
}