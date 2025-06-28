package com.formation.centre.dto;

import com.formation.centre.model.Charge;

public class ChargeDTO {
    private String id;
    private String intitule;
    private String montant;
    private String dateCharge;
    private String proprieteId;
    private String nature;
    private String commentaire;
    private String utilisateurId;
    private String createdAt;
    private String updatedAt;

    // Constructeurs
    public ChargeDTO() {}

    public ChargeDTO(String id, String intitule, String montant, String dateCharge, 
                    String proprieteId, String nature, String commentaire, 
                    String utilisateurId, String createdAt, String updatedAt) {
        this.id = id;
        this.intitule = intitule;
        this.montant = montant;
        this.dateCharge = dateCharge;
        this.proprieteId = proprieteId;
        this.nature = nature;
        this.commentaire = commentaire;
        this.utilisateurId = utilisateurId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Méthode de conversion depuis l'entité
    public static ChargeDTO fromEntity(Charge charge) {
        if (charge == null) {
            return null;
        }

        ChargeDTO dto = new ChargeDTO();
        dto.setId(charge.getId() != null ? charge.getId().toString() : null);
        dto.setIntitule(charge.getIntitule());
        dto.setMontant(charge.getMontant() != null ? charge.getMontant().toPlainString() : null);
        dto.setDateCharge(charge.getDateCharge() != null ? charge.getDateCharge().toString() : null);
        dto.setProprieteId(charge.getPropriete() != null ? charge.getPropriete().getId().toString() : null);
        dto.setNature(charge.getNature() != null ? charge.getNature().name() : null);
        dto.setCommentaire(charge.getCommentaire());
        dto.setUtilisateurId(charge.getUtilisateur() != null ? charge.getUtilisateur().getId().toString() : null);
        dto.setCreatedAt(charge.getCreeLe() != null ? charge.getCreeLe().toString() : null);
        dto.setUpdatedAt(charge.getModifieLe() != null ? charge.getModifieLe().toString() : null);

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

    public String getDateCharge() {
        return dateCharge;
    }

    public void setDateCharge(String dateCharge) {
        this.dateCharge = dateCharge;
    }

    public String getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(String proprieteId) {
        this.proprieteId = proprieteId;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
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