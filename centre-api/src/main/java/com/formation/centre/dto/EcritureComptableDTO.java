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
    private String journalLib;
    private String pieceDate;
    private String documentId;
    private String documentNom;
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

    public String getJournalLib() { return journalLib; }
    public void setJournalLib(String journalLib) { this.journalLib = journalLib; }

    public String getPieceDate() { return pieceDate; }
    public void setPieceDate(String pieceDate) { this.pieceDate = pieceDate; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getDocumentNom() { return documentNom; }
    public void setDocumentNom(String documentNom) { this.documentNom = documentNom; }

    public List<LigneEcritureDTO> getLignes() { return lignes; }
    public void setLignes(List<LigneEcritureDTO> lignes) { this.lignes = lignes; }
}
