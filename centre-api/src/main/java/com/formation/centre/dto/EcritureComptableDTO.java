package com.formation.centre.dto;

public class EcritureComptableDTO {
    private String id;
    private String proprieteId;
    private String dateEcriture;
    private String libelle;
    private String montant;
    private String typeOperation;
    private String categorieComptable;
    private String codeJournal;
    private String tva;
    private String justificatifUrl;

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

    public String getDateEcriture() {
        return dateEcriture;
    }

    public void setDateEcriture(String dateEcriture) {
        this.dateEcriture = dateEcriture;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public String getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(String categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public String getCodeJournal() {
        return codeJournal;
    }

    public void setCodeJournal(String codeJournal) {
        this.codeJournal = codeJournal;
    }

    public String getTva() {
        return tva;
    }

    public void setTva(String tva) {
        this.tva = tva;
    }

    public String getJustificatifUrl() {
        return justificatifUrl;
    }

    public void setJustificatifUrl(String justificatifUrl) {
        this.justificatifUrl = justificatifUrl;
    }
}
