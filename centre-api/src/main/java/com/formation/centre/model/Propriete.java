package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "propriete")
public class Propriete {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_bien")
    private TypeBien typeBien;

    private String nom;
    private String adresse;

    @Column(name = "complement_adresse")
    private String complementAdresse;

    @Column(name = "code_postal")
    private String codePostal;

    private String ville;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @Column(name = "date_livraison")
    private LocalDate dateLivraison;

    @Column(name = "montant_acquisition")
    private BigDecimal montantAcquisition;

    private BigDecimal tantieme;

    @Column(name = "frais_notaire")
    private BigDecimal fraisNotaire;

    @Column(name = "frais_agence")
    private BigDecimal fraisAgence;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    public enum TypeBien {
        Appartement,
        Maison,
        Box,
        Parking
    }

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

    public TypeBien getTypeBien() {
        return typeBien;
    }

    public void setTypeBien(TypeBien typeBien) {
        this.typeBien = typeBien;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getComplementAdresse() {
        return complementAdresse;
    }

    public void setComplementAdresse(String complementAdresse) {
        this.complementAdresse = complementAdresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public LocalDate getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public BigDecimal getMontantAcquisition() {
        return montantAcquisition;
    }

    public void setMontantAcquisition(BigDecimal montantAcquisition) {
        this.montantAcquisition = montantAcquisition;
    }

    public BigDecimal getTantieme() {
        return tantieme;
    }

    public void setTantieme(BigDecimal tantieme) {
        this.tantieme = tantieme;
    }

    public BigDecimal getFraisNotaire() {
        return fraisNotaire;
    }

    public void setFraisNotaire(BigDecimal fraisNotaire) {
        this.fraisNotaire = fraisNotaire;
    }

    public BigDecimal getFraisAgence() {
        return fraisAgence;
    }

    public void setFraisAgence(BigDecimal fraisAgence) {
        this.fraisAgence = fraisAgence;
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
