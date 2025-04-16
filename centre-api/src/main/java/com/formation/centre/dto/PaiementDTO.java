package com.formation.centre.dto;

public class PaiementDTO {
    private String id;
    private String quittanceId;
    private String datePaiement;
    private String montant;
    private String moyenPaiement;
    private String reference;
    private String commentaire;
    private String estValide;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuittanceId() {
        return quittanceId;
    }

    public void setQuittanceId(String quittanceId) {
        this.quittanceId = quittanceId;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getEstValide() {
        return estValide;
    }

    public void setEstValide(String estValide) {
        this.estValide = estValide;
    }
}
