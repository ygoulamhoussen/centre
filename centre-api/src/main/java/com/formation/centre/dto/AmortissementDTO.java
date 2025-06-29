package com.formation.centre.dto;

import com.formation.centre.model.Amortissement;

public class AmortissementDTO {
    private String id;
    private String immobilisationId;
    private String immobilisationIntitule;
    private String annee;
    private String montantAmortissement;
    private String valeurResiduelle;
    private String cumulAmortissements;
    private String tauxAmortissement;
    private String createdAt;
    private String updatedAt;

    // Constructeurs
    public AmortissementDTO() {}

    // Méthode de conversion depuis l'entité
    public static AmortissementDTO fromEntity(Amortissement amortissement) {
        if (amortissement == null) {
            return null;
        }

        AmortissementDTO dto = new AmortissementDTO();
        dto.setId(amortissement.getId() != null ? amortissement.getId().toString() : null);
        dto.setImmobilisationId(amortissement.getImmobilisation() != null ? amortissement.getImmobilisation().getId().toString() : null);
        dto.setImmobilisationIntitule(amortissement.getImmobilisation() != null ? amortissement.getImmobilisation().getIntitule() : null);
        dto.setAnnee(amortissement.getAnnee() != null ? amortissement.getAnnee().toString() : null);
        dto.setMontantAmortissement(amortissement.getMontantAmortissement() != null ? amortissement.getMontantAmortissement().toPlainString() : null);
        dto.setValeurResiduelle(amortissement.getValeurResiduelle() != null ? amortissement.getValeurResiduelle().toPlainString() : null);
        dto.setCumulAmortissements(amortissement.getCumulAmortissements() != null ? amortissement.getCumulAmortissements().toPlainString() : null);
        dto.setTauxAmortissement(amortissement.getTauxAmortissement() != null ? amortissement.getTauxAmortissement().toPlainString() : null);
        dto.setCreatedAt(amortissement.getCreeLe() != null ? amortissement.getCreeLe().toString() : null);
        dto.setUpdatedAt(amortissement.getModifieLe() != null ? amortissement.getModifieLe().toString() : null);

        return dto;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImmobilisationId() {
        return immobilisationId;
    }

    public void setImmobilisationId(String immobilisationId) {
        this.immobilisationId = immobilisationId;
    }

    public String getImmobilisationIntitule() {
        return immobilisationIntitule;
    }

    public void setImmobilisationIntitule(String immobilisationIntitule) {
        this.immobilisationIntitule = immobilisationIntitule;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getMontantAmortissement() {
        return montantAmortissement;
    }

    public void setMontantAmortissement(String montantAmortissement) {
        this.montantAmortissement = montantAmortissement;
    }

    public String getValeurResiduelle() {
        return valeurResiduelle;
    }

    public void setValeurResiduelle(String valeurResiduelle) {
        this.valeurResiduelle = valeurResiduelle;
    }

    public String getCumulAmortissements() {
        return cumulAmortissements;
    }

    public void setCumulAmortissements(String cumulAmortissements) {
        this.cumulAmortissements = cumulAmortissements;
    }

    public String getTauxAmortissement() {
        return tauxAmortissement;
    }

    public void setTauxAmortissement(String tauxAmortissement) {
        this.tauxAmortissement = tauxAmortissement;
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
