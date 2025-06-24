package com.formation.centre.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "amortissement")
public class Amortissement {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_id")
    private Propriete propriete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "composition_id", nullable = true)
    private CompositionAcquisition composition;

    private Integer annee;
    private BigDecimal montantAmorti;
    private BigDecimal valeurResiduelle;
    private String categorie;

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

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }

    public CompositionAcquisition getComposition() {
        return composition;
    }

    public void setComposition(CompositionAcquisition composition) {
        this.composition = composition;
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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
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
