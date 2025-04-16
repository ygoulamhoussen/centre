package com.formation.centre.dto;

public class ImmobilisationDTO {
    private String id;
    private String proprieteId;
    private String nom;
    private String categorie;
    private String valeur;
    private String dureeAmortissement;
    private String dateMiseEnService;

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getDureeAmortissement() {
        return dureeAmortissement;
    }

    public void setDureeAmortissement(String dureeAmortissement) {
        this.dureeAmortissement = dureeAmortissement;
    }

    public String getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(String dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }
}
