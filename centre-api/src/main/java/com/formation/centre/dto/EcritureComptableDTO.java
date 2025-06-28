package com.formation.centre.dto;

import com.formation.centre.model.EcritureComptable;

public class EcritureComptableDTO {
    private String id;
    private String dateEcriture;
    private String montant;
    private String type;
    private String proprieteId;
    private String chargeId;
    private String recetteId;
    private String commentaire;
    private String utilisateurId;
    private String createdAt;
    private String updatedAt;

    // Constructeurs
    public EcritureComptableDTO() {}

    public EcritureComptableDTO(String id, String dateEcriture, String montant, String type, 
                               String proprieteId, String chargeId, String recetteId, String commentaire, 
                               String utilisateurId, String createdAt, String updatedAt) {
        this.id = id;
        this.dateEcriture = dateEcriture;
        this.montant = montant;
        this.type = type;
        this.proprieteId = proprieteId;
        this.chargeId = chargeId;
        this.recetteId = recetteId;
        this.commentaire = commentaire;
        this.utilisateurId = utilisateurId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Méthode de conversion depuis l'entité
    public static EcritureComptableDTO fromEntity(EcritureComptable ecriture) {
        if (ecriture == null) {
            return null;
        }

        EcritureComptableDTO dto = new EcritureComptableDTO();
        dto.setId(ecriture.getId() != null ? ecriture.getId().toString() : null);
        dto.setDateEcriture(ecriture.getDateEcriture() != null ? ecriture.getDateEcriture().toString() : null);
        dto.setMontant(ecriture.getMontant() != null ? ecriture.getMontant().toPlainString() : null);
        dto.setType(ecriture.getType() != null ? ecriture.getType().name() : null);
        dto.setProprieteId(ecriture.getPropriete() != null ? ecriture.getPropriete().getId().toString() : null);
        dto.setChargeId(ecriture.getCharge() != null ? ecriture.getCharge().getId().toString() : null);
        dto.setRecetteId(ecriture.getRecette() != null ? ecriture.getRecette().getId().toString() : null);
        dto.setCommentaire(ecriture.getCommentaire());
        dto.setUtilisateurId(ecriture.getUtilisateur() != null ? ecriture.getUtilisateur().getId().toString() : null);
        dto.setCreatedAt(ecriture.getCreeLe() != null ? ecriture.getCreeLe().toString() : null);
        dto.setUpdatedAt(ecriture.getModifieLe() != null ? ecriture.getModifieLe().toString() : null);

        return dto;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateEcriture() {
        return dateEcriture;
    }

    public void setDateEcriture(String dateEcriture) {
        this.dateEcriture = dateEcriture;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProprieteId() {
        return proprieteId;
    }

    public void setProprieteId(String proprieteId) {
        this.proprieteId = proprieteId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getRecetteId() {
        return recetteId;
    }

    public void setRecetteId(String recetteId) {
        this.recetteId = recetteId;
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
