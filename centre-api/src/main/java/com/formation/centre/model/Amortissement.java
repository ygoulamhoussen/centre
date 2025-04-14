package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "amortissement")
public class Amortissement {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "immobilisation_id")
    private Immobilisation immobilisation;

    private Integer annee;

    @Column(name = "montant_amorti")
    private BigDecimal montantAmorti;

    @Column(name = "valeur_residuelle")
    private BigDecimal valeurResiduelle;

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

    public BigDecimal getMontantAmorti() {
        return montantAmorti;
    }

    public void setMontantAmorti(BigDecimal montantAmorti) {
        this.montantAmorti = montantAmorti;
    }

    public BigDecimal getValeurResiduelle() {
        return valeurResiduelle;
    }

    public void setValeurResiduelle(BigDecimal valeurResiduelle) {
        this.valeurResiduelle = valeurResiduelle;
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
