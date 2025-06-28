package com.formation.centre.dto;

import com.formation.centre.model.Recette;

public class RecetteDTO {
    private String id;
    private String intitule;
    private String montant;
    private String dateRecette;
    private String proprieteId;
    private String proprieteNom;
    private String type;
    private String quittanceId;
    private String commentaire;
    private String utilisateurId;
    private String documentId;
    private String documentNom;
    private String createdAt;
    private String updatedAt;

    // Constructeurs
    public RecetteDTO() {}

    public RecetteDTO(String id, String intitule, String montant, String dateRecette, 
                     String proprieteId, String proprieteNom, String type, String quittanceId, 
                     String commentaire, String utilisateurId, String documentId, String documentNom,
                     String createdAt, String updatedAt) {
        this.id = id;
        this.intitule = intitule;
        this.montant = montant;
        this.dateRecette = dateRecette;
        this.proprieteId = proprieteId;
        this.proprieteNom = proprieteNom;
        this.type = type;
        this.quittanceId = quittanceId;
        this.commentaire = commentaire;
        this.utilisateurId = utilisateurId;
        this.documentId = documentId;
        this.documentNom = documentNom;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Méthode de conversion depuis l'entité
    public static RecetteDTO fromEntity(Recette recette) {
        if (recette == null) {
            return null;
        }

        RecetteDTO dto = new RecetteDTO();
        dto.setId(recette.getId() != null ? recette.getId().toString() : null);
        dto.setIntitule(recette.getIntitule());
        dto.setMontant(recette.getMontant() != null ? recette.getMontant().toPlainString() : null);
        dto.setDateRecette(recette.getDateRecette() != null ? recette.getDateRecette().toString() : null);
        dto.setProprieteId(recette.getPropriete() != null ? recette.getPropriete().getId().toString() : null);
        dto.setProprieteNom(recette.getPropriete() != null ? recette.getPropriete().getNom() : null);
        dto.setType(recette.getType() != null ? recette.getType().name() : null);
        dto.setQuittanceId(recette.getQuittance() != null ? recette.getQuittance().getId().toString() : null);
        dto.setCommentaire(recette.getCommentaire());
        dto.setUtilisateurId(recette.getUtilisateur() != null ? recette.getUtilisateur().getId().toString() : null);
        dto.setDocumentId(recette.getDocument() != null ? recette.getDocument().getId().toString() : null);
        dto.setDocumentNom(recette.getDocument() != null ? recette.getDocument().getTitre() : null);
        dto.setCreatedAt(recette.getCreeLe() != null ? recette.getCreeLe().toString() : null);
        dto.setUpdatedAt(recette.getModifieLe() != null ? recette.getModifieLe().toString() : null);

        return dto;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDateRecette() {
        return dateRecette;
    }

    public void setDateRecette(String dateRecette) {
        this.dateRecette = dateRecette;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuittanceId() {
        return quittanceId;
    }

    public void setQuittanceId(String quittanceId) {
        this.quittanceId = quittanceId;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentNom() {
        return documentNom;
    }

    public void setDocumentNom(String documentNom) {
        this.documentNom = documentNom;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
} 