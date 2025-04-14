package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cloture_exercice")
public class ClotureExercice {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "annee_fiscale")
    private Integer anneeFiscale;

    @Column(name = "total_loyers")
    private BigDecimal totalLoyers;

    @Column(name = "total_charges")
    private BigDecimal totalCharges;

    @Column(name = "total_amortissements")
    private BigDecimal totalAmortissements;

    @Column(name = "resultat_comptable")
    private BigDecimal resultatComptable;

    @Column(name = "revenu_imposable")
    private BigDecimal revenuImposable;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    // Getters & Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Integer getAnneeFiscale() {
        return anneeFiscale;
    }

    public void setAnneeFiscale(Integer anneeFiscale) {
        this.anneeFiscale = anneeFiscale;
    }

    public BigDecimal getTotalLoyers() {
        return totalLoyers;
    }

    public void setTotalLoyers(BigDecimal totalLoyers) {
        this.totalLoyers = totalLoyers;
    }

    public BigDecimal getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(BigDecimal totalCharges) {
        this.totalCharges = totalCharges;
    }

    public BigDecimal getTotalAmortissements() {
        return totalAmortissements;
    }

    public void setTotalAmortissements(BigDecimal totalAmortissements) {
        this.totalAmortissements = totalAmortissements;
    }

    public BigDecimal getResultatComptable() {
        return resultatComptable;
    }

    public void setResultatComptable(BigDecimal resultatComptable) {
        this.resultatComptable = resultatComptable;
    }

    public BigDecimal getRevenuImposable() {
        return revenuImposable;
    }

    public void setRevenuImposable(BigDecimal revenuImposable) {
        this.revenuImposable = revenuImposable;
    }

    public LocalDate getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
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
