package com.formation.centre.dto;

/**
 * Data Transfer Object pour les locations,
 * inclut les noms pour affichage
 */
public class LocationDTO {
    private String id;
    private String proprieteId;
    private String proprieteNom;
    private String locataireId;
    private String locataireNom;
    private String locatairePrenom;
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

    public String getProprieteNom() {
        return proprieteNom;
    }

    public void setProprieteNom(String proprieteNom) {
        this.proprieteNom = proprieteNom;
    }

    public String getLocataireId() {
        return locataireId;
    }

    public void setLocataireId(String locataireId) {
        this.locataireId = locataireId;
    }

    public String getLocataireNom() {
        return locataireNom;
    }

    public void setLocataireNom(String locataireNom) {
        this.locataireNom = locataireNom;
    }

    public String getLocatairePrenom() {
        return locatairePrenom;
    }

    public void setLocatairePrenom(String locatairePrenom) {
        this.locatairePrenom = locatairePrenom;
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
