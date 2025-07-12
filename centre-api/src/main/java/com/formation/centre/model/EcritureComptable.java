package com.formation.centre.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "ecriture_comptable")
public class EcritureComptable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date_ecriture", nullable = false)
    private LocalDate dateEcriture;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "journal_code", nullable = false, length = 10)
    private String journalCode;

    @Column(name = "numero_piece", length = 50)
    private String numeroPiece;

    @Column(name = "journal_lib", length = 100)
    private String journalLib;

    @Column(name = "piece_date")
    private LocalDate pieceDate;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "ecriture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneEcriture> lignes = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public EcritureComptable() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDate getDateEcriture() { return dateEcriture; }
    public void setDateEcriture(LocalDate dateEcriture) { this.dateEcriture = dateEcriture; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getJournalCode() { return journalCode; }
    public void setJournalCode(String journalCode) { this.journalCode = journalCode; }

    public String getNumeroPiece() { return numeroPiece; }
    public void setNumeroPiece(String numeroPiece) { this.numeroPiece = numeroPiece; }

    public String getJournalLib() { return journalLib; }
    public void setJournalLib(String journalLib) { this.journalLib = journalLib; }

    public LocalDate getPieceDate() { return pieceDate; }
    public void setPieceDate(LocalDate pieceDate) { this.pieceDate = pieceDate; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public List<LigneEcriture> getLignes() { return lignes; }
    public void setLignes(List<LigneEcriture> lignes) { this.lignes = lignes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}