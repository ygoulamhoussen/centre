package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "propriete")
public class Propriete {

    @Id
    @GeneratedValue
    private UUID id;

    private String nom;
    private String adresse;
    private String complementAdresse;
    private String codePostal;
    private String ville;

    @Enumerated(EnumType.STRING)
    private TypeBien typeBien;

    private LocalDate dateAcquisition;
    private LocalDate dateLivraison;
    private BigDecimal montantAcquisition;
    private BigDecimal tantieme;
    private BigDecimal fraisNotaire;
    private BigDecimal fraisAgence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "propriete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompositionAcquisition> compositions;

    public enum TypeBien {
        APPARTEMENT, MAISON, BOX, PARKING
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public TypeBien getTypeBien() {
        return typeBien;
    }

    public void setTypeBien(TypeBien typeBien) {
        this.typeBien = typeBien;
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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<CompositionAcquisition> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<CompositionAcquisition> compositions) {
        this.compositions = compositions;
    }
}