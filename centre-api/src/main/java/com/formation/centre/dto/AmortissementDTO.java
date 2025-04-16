package com.formation.centre.dto;

public class AmortissementDTO {
    private String id;
    private String immobilisationId;
    private String annee;
    private String montantAmorti;
    private String valeurResiduelle;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImmobilisationId() {
        return immobilisationId;
    }

    public void setImmobilisationId(String immobilisationId) {
        this.immobilisationId = immobilisationId;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getMontantAmorti() {
        return montantAmorti;
    }

    public void setMontantAmorti(String montantAmorti) {
        this.montantAmorti = montantAmorti;
    }

    public String getValeurResiduelle() {
        return valeurResiduelle;
    }

    public void setValeurResiduelle(String valeurResiduelle) {
        this.valeurResiduelle = valeurResiduelle;
    }
}
