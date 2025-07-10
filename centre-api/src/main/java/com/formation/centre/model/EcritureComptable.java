package com.formation.centre.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

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

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}