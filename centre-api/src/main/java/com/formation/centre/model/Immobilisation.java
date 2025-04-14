package com.formation.centre.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "immobilisation")
public class Immobilisation {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_id")
    private Propriete propriete;

    private String nom;

    @Enumerated(EnumType.STRING)
    private CategorieImmobilisation categorie;

    private BigDecimal valeur;

    @Column(name = "duree_amortissement")
    private Integer dureeAmortissement;

    @Column(name = "date_mise_en_service")
    private LocalDate dateMiseEnService;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    public enum CategorieImmobilisation {
        Terrain,
        Structure,
        Façades,
        Toiture,
        InstallationsElectriques,
        Plomberie,
        Chauffage,
        AgencementsInterieurs,
        CuisineEquipee,
        Mobilier,
        Ascenseur,
        Autre
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public CategorieImmobilisation getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieImmobilisation categorie) {
        this.categorie = categorie;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public Integer getDureeAmortissement() {
        return dureeAmortissement;
    }

    public void setDureeAmortissement(Integer dureeAmortissement) {
        this.dureeAmortissement = dureeAmortissement;
    }

    public LocalDate getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDate dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
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