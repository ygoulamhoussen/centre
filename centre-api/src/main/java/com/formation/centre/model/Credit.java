package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit")
public class Credit {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_id")
    private Propriete propriete;

    private String banque;

    @Column(name = "montant_emprunte")
    private BigDecimal montantEmprunte;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "duree_mois")
    private Integer dureeMois;

    @Column(name = "taux_interet_annuel")
    private BigDecimal tauxInteretAnnuel;

    private BigDecimal mensualite;

    @Column(name = "assurance_mensuelle")
    private BigDecimal assuranceMensuelle;

    @Column(name = "frais_dossier")
    private BigDecimal fraisDossier;

    @Column(name = "frais_garantie")
    private BigDecimal fraisGarantie;

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

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public BigDecimal getMontantEmprunte() {
        return montantEmprunte;
    }

    public void setMontantEmprunte(BigDecimal montantEmprunte) {
        this.montantEmprunte = montantEmprunte;
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

    public Integer getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(Integer dureeMois) {
        this.dureeMois = dureeMois;
    }

    public BigDecimal getTauxInteretAnnuel() {
        return tauxInteretAnnuel;
    }

    public void setTauxInteretAnnuel(BigDecimal tauxInteretAnnuel) {
        this.tauxInteretAnnuel = tauxInteretAnnuel;
    }

    public BigDecimal getMensualite() {
        return mensualite;
    }

    public void setMensualite(BigDecimal mensualite) {
        this.mensualite = mensualite;
    }

    public BigDecimal getAssuranceMensuelle() {
        return assuranceMensuelle;
    }

    public void setAssuranceMensuelle(BigDecimal assuranceMensuelle) {
        this.assuranceMensuelle = assuranceMensuelle;
    }

    public BigDecimal getFraisDossier() {
        return fraisDossier;
    }

    public void setFraisDossier(BigDecimal fraisDossier) {
        this.fraisDossier = fraisDossier;
    }

    public BigDecimal getFraisGarantie() {
        return fraisGarantie;
    }

    public void setFraisGarantie(BigDecimal fraisGarantie) {
        this.fraisGarantie = fraisGarantie;
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