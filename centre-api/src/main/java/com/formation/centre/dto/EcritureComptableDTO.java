package com.formation.centre.dto;

import java.util.List;

public class EcritureComptableDTO {
    private String id;
    private String dateEcriture;
    private String libelle;
    private String journalCode;
    private String numeroPiece;
    private String utilisateurId;
    private String createdAt;
    private List<LigneEcritureDTO> lignes;

    public EcritureComptableDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDateEcriture() { return dateEcriture; }
    public void setDateEcriture(String dateEcriture) { this.dateEcriture = dateEcriture; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getJournalCode() { return journalCode; }
    public void setJournalCode(String journalCode) { this.journalCode = journalCode; }

    public String getNumeroPiece() { return numeroPiece; }
    public void setNumeroPiece(String numeroPiece) { this.numeroPiece = numeroPiece; }

    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<LigneEcritureDTO> getLignes() { return lignes; }
    public void setLignes(List<LigneEcritureDTO> lignes) { this.lignes = lignes; }
}
