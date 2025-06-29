package com.formation.centre.dto;

import com.formation.centre.model.Immobilisation;

public class ImmobilisationDTO {
    private String id;
    private String intitule;
    private String montant;
    private String dateAcquisition;
    private String dureeAmortissement;
    private String typeImmobilisation;
    private String categorieFiscale;
    private String valeurTerrain;
    private String commentaire;
    private String proprieteId;
    private String proprieteNom;
    private String utilisateurId;
    private String createdAt;
    private String updatedAt;

    // Constructeurs
    public ImmobilisationDTO() {}

    // Méthode de conversion depuis l'entité
    public static ImmobilisationDTO fromEntity(Immobilisation immobilisation) {
        if (immobilisation == null) {
            return null;
        }

        ImmobilisationDTO dto = new ImmobilisationDTO();
        dto.setId(immobilisation.getId() != null ? immobilisation.getId().toString() : null);
        dto.setIntitule(immobilisation.getIntitule());
        dto.setMontant(immobilisation.getMontant() != null ? immobilisation.getMontant().toPlainString() : null);
        dto.setDateAcquisition(immobilisation.getDateAcquisition() != null ? immobilisation.getDateAcquisition().toString() : null);
        dto.setDureeAmortissement(immobilisation.getDureeAmortissement() != null ? immobilisation.getDureeAmortissement().toString() : null);
        dto.setTypeImmobilisation(immobilisation.getTypeImmobilisation());
        dto.setCategorieFiscale(immobilisation.getCategorieFiscale() != null ? immobilisation.getCategorieFiscale().name() : null);
        dto.setValeurTerrain(immobilisation.getValeurTerrain() != null ? immobilisation.getValeurTerrain().toPlainString() : null);
        dto.setCommentaire(immobilisation.getCommentaire());
        dto.setProprieteId(immobilisation.getPropriete() != null ? immobilisation.getPropriete().getId().toString() : null);
        dto.setProprieteNom(immobilisation.getPropriete() != null ? immobilisation.getPropriete().getNom() : null);
        dto.setUtilisateurId(immobilisation.getUtilisateur() != null ? immobilisation.getUtilisateur().getId().toString() : null);
        dto.setCreatedAt(immobilisation.getCreeLe() != null ? immobilisation.getCreeLe().toString() : null);
        dto.setUpdatedAt(immobilisation.getModifieLe() != null ? immobilisation.getModifieLe().toString() : null);

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

    public String getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(String dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public String getDureeAmortissement() {
        return dureeAmortissement;
    }

    public void setDureeAmortissement(String dureeAmortissement) {
        this.dureeAmortissement = dureeAmortissement;
    }

    public String getTypeImmobilisation() {
        return typeImmobilisation;
    }

    public void setTypeImmobilisation(String typeImmobilisation) {
        this.typeImmobilisation = typeImmobilisation;
    }

    public String getCategorieFiscale() {
        return categorieFiscale;
    }

    public void setCategorieFiscale(String categorieFiscale) {
        this.categorieFiscale = categorieFiscale;
    }

    public String getValeurTerrain() {
        return valeurTerrain;
    }

    public void setValeurTerrain(String valeurTerrain) {
        this.valeurTerrain = valeurTerrain;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
