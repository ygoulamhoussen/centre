package com.formation.centre.dto;

public class DocumentDTO {
    private String id;
    private String utilisateurId;
    private String proprieteId;
    private String locataireId;
    private String immobilisationId;
    private String ecritureId;
    private String typeDocument;
    private String titre;
    private String urlFichier;
    private String dateDocument;
    private String contenu; // pour les téléchargements si besoin



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

    public String getImmobilisationId() {
        return immobilisationId;
    }

    public void setImmobilisationId(String immobilisationId) {
        this.immobilisationId = immobilisationId;
    }

    public String getEcritureId() {
        return ecritureId;
    }

    public void setEcritureId(String ecritureId) {
        this.ecritureId = ecritureId;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrlFichier() {
        return urlFichier;
    }

    public void setUrlFichier(String urlFichier) {
        this.urlFichier = urlFichier;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
