package com.formation.centre.dto;

public class QuittanceDTO {
    private String id;
    private String locationId;
    private String dateDebut;
    private String dateFin;
    private String dateEmission;
    private String dateEcheance;
    private String montantLoyer;
    private String montantCharges;
    private String montantTotal;
    private String statut;
    private Boolean inclureCaution;
    private String depotGarantie;   // ← nouveau
    private String proprieteNom;
    private String locataireNom;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(String dateEmission) {
        this.dateEmission = dateEmission;
    }

    public String getMontantLoyer() {
        return montantLoyer;
    }

    public void setMontantLoyer(String montantLoyer) {
        this.montantLoyer = montantLoyer;
    }

    public String getMontantCharges() {
        return montantCharges;
    }

    public void setMontantCharges(String montantCharges) {
        this.montantCharges = montantCharges;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Boolean getInclureCaution() {
        return inclureCaution;
    }

    public void setInclureCaution(Boolean inclureCaution) {
        this.inclureCaution = inclureCaution;
    }

    public String getDepotGarantie() {
        return depotGarantie;
    }

    public void setDepotGarantie(String depotGarantie) {
        this.depotGarantie = depotGarantie;
    }

    public String getProprieteNom() {
        return proprieteNom;
    }

    public void setProprieteNom(String proprieteNom) {
        this.proprieteNom = proprieteNom;
    }

    public String getLocataireNom() {
        return locataireNom;
    }

    public void setLocataireNom(String locataireNom) {
        this.locataireNom = locataireNom;
    }
}