package com.formation.centre.dto;

public class LocationDetailDTO {
    private String id;
    private String dateDebut;
    private String dateFin;
    private String frequenceLoyer;
    private String jourEcheance;
    private String loyerMensuel;
    private String chargesMensuelles;
    private String depotGarantie;

    private LocataireDTO locataire;
    private ProprieteDTO propriete;

    // Getters / Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocataireDTO getLocataire() {
        return locataire;
    }

    public void setLocataire(LocataireDTO locataire) {
        this.locataire = locataire;
    }

    public ProprieteDTO getPropriete() {
        return propriete;
    }

    public void setPropriete(ProprieteDTO propriete) {
        this.propriete = propriete;
    }
    
}
