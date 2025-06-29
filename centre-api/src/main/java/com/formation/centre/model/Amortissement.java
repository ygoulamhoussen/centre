package com.formation.centre.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "amortissement")
public class Amortissement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "immobilisation_id", nullable = false)
    private Immobilisation immobilisation;

    @Column(name = "annee", nullable = false)
    private Integer annee;

    @Column(name = "montant_amortissement", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantAmortissement;

    @Column(name = "valeur_residuelle", nullable = false, precision = 10, scale = 2)
    private BigDecimal valeurResiduelle;

    @Column(name = "cumul_amortissements", nullable = false, precision = 10, scale = 2)
    private BigDecimal cumulAmortissements;

    @Column(name = "taux_amortissement", nullable = false, precision = 5, scale = 2)
    private BigDecimal tauxAmortissement;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    // Constructeurs
    public Amortissement() {}

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Immobilisation getImmobilisation() {
        return immobilisation;
    }

    public void setImmobilisation(Immobilisation immobilisation) {
        this.immobilisation = immobilisation;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public BigDecimal getMontantAmortissement() {
        return montantAmortissement;
    }

    public void setMontantAmortissement(BigDecimal montantAmortissement) {
        this.montantAmortissement = montantAmortissement;
    }

    public BigDecimal getValeurResiduelle() {
        return valeurResiduelle;
    }

    public void setValeurResiduelle(BigDecimal valeurResiduelle) {
        this.valeurResiduelle = valeurResiduelle;
    }

    public BigDecimal getCumulAmortissements() {
        return cumulAmortissements;
    }

    public void setCumulAmortissements(BigDecimal cumulAmortissements) {
        this.cumulAmortissements = cumulAmortissements;
    }

    public BigDecimal getTauxAmortissement() {
        return tauxAmortissement;
    }

    public void setTauxAmortissement(BigDecimal tauxAmortissement) {
        this.tauxAmortissement = tauxAmortissement;
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
