package com.formation.centre.dto;

import com.formation.centre.model.CompositionAcquisition;

public class CompositionAcquisitionDTO {
    private String id;
    private String proprieteId;
    private String categorie;
    private String montant;
    private String description;
    private Integer duree; // durée d'amortissement en années

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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }
    
    // Méthode utilitaire pour créer un DTO à partir d'une entité
    public static CompositionAcquisitionDTO fromEntity(CompositionAcquisition entity) {
        if (entity == null) {
            return null;
        }
        CompositionAcquisitionDTO dto = new CompositionAcquisitionDTO();
        dto.setId(entity.getId() != null ? entity.getId().toString() : null);
        dto.setProprieteId(entity.getPropriete() != null ? entity.getPropriete().getId().toString() : null);
        dto.setCategorie(entity.getCategorie());
        dto.setMontant(entity.getMontant() != null ? entity.getMontant().toPlainString() : null);
        dto.setDescription(entity.getDescription());
        dto.setDuree(entity.getDuree());
        return dto;
    }
}
