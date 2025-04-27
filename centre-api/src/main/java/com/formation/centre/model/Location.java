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
@Table(name = "location")
public class Location {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_id")
    private Propriete propriete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locataire_id")
    private Locataire locataire;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "loyer_mensuel")
    private BigDecimal loyerMensuel;

    @Column(name = "charges_mensuelles")
    private BigDecimal chargesMensuelles;

    @Column(name = "depot_garantie")
    private BigDecimal depotGarantie;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequence_loyer")
    private FrequenceLoyer frequenceLoyer;

    @Column(name = "jour_echeance")
    private Integer jourEcheance;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    public enum FrequenceLoyer {
        MENSUEL, TRIMESTRIEL, ANNUEL
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

    public Locataire getLocataire() {
        return locataire;
    }

    public void setLocataire(Locataire locataire) {
        this.locataire = locataire;
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

    public BigDecimal getLoyerMensuel() {
        return loyerMensuel;
    }

    public void setLoyerMensuel(BigDecimal loyerMensuel) {
        this.loyerMensuel = loyerMensuel;
    }

    public BigDecimal getChargesMensuelles() {
        return chargesMensuelles;
    }

    public void setChargesMensuelles(BigDecimal chargesMensuelles) {
        this.chargesMensuelles = chargesMensuelles;
    }

    public BigDecimal getDepotGarantie() {
        return depotGarantie;
    }

    public void setDepotGarantie(BigDecimal depotGarantie) {
        this.depotGarantie = depotGarantie;
    }

    public FrequenceLoyer getFrequenceLoyer() {
        return frequenceLoyer;
    }

    public void setFrequenceLoyer(FrequenceLoyer frequenceLoyer) {
        this.frequenceLoyer = frequenceLoyer;
    }

    public Integer getJourEcheance() {
        return jourEcheance;
    }

    public void setJourEcheance(Integer jourEcheance) {
        this.jourEcheance = jourEcheance;
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
