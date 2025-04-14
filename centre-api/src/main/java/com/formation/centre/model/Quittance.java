package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(name = "montant_loyer")
    private BigDecimal montantLoyer;

    @Column(name = "montant_charges")
    private BigDecimal montantCharges;

    @Column(name = "montant_total")
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    private StatutQuittance statut;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    public enum StatutQuittance {
        Payee,
        Partielle,
        Impayee
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

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
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
