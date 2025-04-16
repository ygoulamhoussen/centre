package com.formation.centre.dto;

public class LocationDTO {
    private String id;
    private String proprieteId;
    private String locataireId;
    private String dateDebut;
    private String dateFin;
    private String loyerMensuel;
    private String chargesMensuelles;
    private String depotGarantie;
    private String frequenceLoyer;
    private String jourEcheance;

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

    public String getLocataireId() {
        return locataireId;
    }

    public void setLocataireId(String locataireId) {
        this.locataireId = locataireId;
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

    public String getLoyerMensuel() {
        return loyerMensuel;
    }

    public void setLoyerMensuel(String loyerMensuel) {
        this.loyerMensuel = loyerMensuel;
    }

    public String getChargesMensuelles() {
        return chargesMensuelles;
    }

    public void setChargesMensuelles(String chargesMensuelles) {
        this.chargesMensuelles = chargesMensuelles;
    }

    public String getDepotGarantie() {
        return depotGarantie;
    }

    public void setDepotGarantie(String depotGarantie) {
        this.depotGarantie = depotGarantie;
    }

    public String getFrequenceLoyer() {
        return frequenceLoyer;
    }

    public void setFrequenceLoyer(String frequenceLoyer) {
        this.frequenceLoyer = frequenceLoyer;
    }

    public String getJourEcheance() {
        return jourEcheance;
    }

    public void setJourEcheance(String jourEcheance) {
        this.jourEcheance = jourEcheance;
    }
}