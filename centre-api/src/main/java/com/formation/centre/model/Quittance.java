package com.formation.centre.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "quittance")
public class Quittance {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "date_emission")
    private LocalDate dateEmission;

    @Column(name = "date_echeance")  // ← nouvelle colonne
    private LocalDate dateEcheance;

    @Column(name = "inclure_caution") // ← nouvelle colonne
    private Boolean inclureCaution = Boolean.FALSE;

    @Column(name = "depot_garantie")       // ← nouveau champ
    private BigDecimal depotGarantie;

    @Column(name = "montant_loyer")
    private BigDecimal montantLoyer;

    @Column(name = "montant_charges")
    private BigDecimal montantCharges;

    @Column(name = "montant_caution")
    private BigDecimal montantCaution;

    @Enumerated(EnumType.STRING)
    private StatutQuittance statut;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    @Column(name = "montant_total")
    private BigDecimal montantTotal;

    public BigDecimal getMontantCaution() {
        return montantCaution;
    }

    public void setMontantCaution(BigDecimal montantCaution) {
        this.montantCaution = montantCaution;
    }

    public BigDecimal getDepotGarantie() {
        return depotGarantie;
    }

    public void setDepotGarantie(BigDecimal depotGarantie) {
        this.depotGarantie = depotGarantie;
    }

    public Boolean getInclureCaution() {
        return inclureCaution;
    }

    public void setInclureCaution(Boolean inclureCaution) {
        this.inclureCaution = inclureCaution;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public enum StatutQuittance {
        PAYEE,
        PARTIELLE,
        IMPAYEE
    }

    // Getters & Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public BigDecimal getMontantLoyer() {
        return montantLoyer;
    }

    public void setMontantLoyer(BigDecimal montantLoyer) {
        this.montantLoyer = montantLoyer;
    }

    public BigDecimal getMontantCharges() {
        return montantCharges;
    }

    public void setMontantCharges(BigDecimal montantCharges) {
        this.montantCharges = montantCharges;
    }


    public StatutQuittance getStatut() {
        return statut;
    }

    public void setStatut(StatutQuittance statut) {
        this.statut = statut;
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
