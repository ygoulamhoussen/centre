package com.formation.centre.dto;

import java.util.List;

public class ProprieteDTO {
    private String id;
    private String nom;
    private String adresse;
    private String complementAdresse;
    private String codePostal;
    private String ville;
    private String typeBien;
    private String dateAcquisition;
    private String dateLivraison;
    private String montantAcquisition;
    private String tantieme;
    private String fraisNotaire;
    private String fraisAgence;
    private List<CompositionAcquisitionDTO> compositions;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getComplementAdresse() {
        return complementAdresse;
    }

    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTypeBien() {
        return typeBien;
    }

    public void setTypeBien(String typeBien) {
        this.typeBien = typeBien;
    }

    public String getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(String dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public String getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(String dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getMontantAcquisition() {
        return montantAcquisition;
    }

    public void setMontantAcquisition(String montantAcquisition) {
        this.montantAcquisition = montantAcquisition;
    }

    public String getTantieme() {
        return tantieme;
    }

    public void setTantieme(String tantieme) {
        this.tantieme = tantieme;
    }

    public String getFraisNotaire() {
        return fraisNotaire;
    }

    public void setFraisNotaire(String fraisNotaire) {
        this.fraisNotaire = fraisNotaire;
    }

    public String getFraisAgence() {
        return fraisAgence;
    }

    public void setFraisAgence(String fraisAgence) {
        this.fraisAgence = fraisAgence;
    }

    public List<CompositionAcquisitionDTO> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<CompositionAcquisitionDTO> compositions) {
        this.compositions = compositions;
    }
}
