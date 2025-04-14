package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ecriture_comptable")
public class EcritureComptable {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_id")
    private Propriete propriete;

    @Column(name = "date_ecriture")
    private LocalDate dateEcriture;

    private String libelle;
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_operation")
    private TypeOperation typeOperation;

    @Column(name = "categorie_comptable")
    private String categorieComptable;

    @Enumerated(EnumType.STRING)
    @Column(name = "code_journal")
    private CodeJournal codeJournal;

    private BigDecimal tva;

    @Column(name = "justificatif_url")
    private String justificatifUrl;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    public enum TypeOperation {
        Recette,
        Charge,
        AchatImmobilisation,
        Amortissement,
        InteretEmprunt,
        AssuranceCredit,
        RemboursementCredit,
        Autre
    }

    public enum CodeJournal {
        AC, VE, BQ, OD, AN
    }

    // Getters & Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }

    public LocalDate getDateEcriture() {
        return dateEcriture;
    }

    public void setDateEcriture(LocalDate dateEcriture) {
        this.dateEcriture = dateEcriture;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public String getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(String categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public CodeJournal getCodeJournal() {
        return codeJournal;
    }

    public void setCodeJournal(CodeJournal codeJournal) {
        this.codeJournal = codeJournal;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public String getJustificatifUrl() {
        return justificatifUrl;
    }

    public void setJustificatifUrl(String justificatifUrl) {
        this.justificatifUrl = justificatifUrl;
    }

    public LocalDateTime getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
    }

    public LocalDateTime getModifieLe() {
        return modifieLe;
    }

    public void setModifieLe(LocalDateTime modifieLe) {
        this.modifieLe = modifieLe;
    }
}