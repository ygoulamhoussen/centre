package com.formation.centre.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "immobilisation")
public class Immobilisation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "intitule", nullable = false)
    private String intitule;

    @Column(name = "montant", nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_acquisition", nullable = false)
    private LocalDate dateAcquisition;

    @Column(name = "duree_amortissement", nullable = false)
    private Integer dureeAmortissement; // en années

    @Column(name = "type_immobilisation", nullable = false)
    private String typeImmobilisation;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie_fiscale", nullable = false)
    private CategorieFiscale categorieFiscale;

    @Column(name = "valeur_terrain", precision = 10, scale = 2)
    private BigDecimal valeurTerrain; // Pour les biens immobiliers

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "propriete_id", nullable = false)
    private Propriete propriete;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "immobilisation")
    private List<EcritureComptable> ecrituresComptables;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    // Constructeurs
    public Immobilisation() {}

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public Integer getDureeAmortissement() {
        return dureeAmortissement;
    }

    public void setDureeAmortissement(Integer dureeAmortissement) {
        this.dureeAmortissement = dureeAmortissement;
    }

    public String getTypeImmobilisation() {
        return typeImmobilisation;
    }

    public void setTypeImmobilisation(String typeImmobilisation) {
        this.typeImmobilisation = typeImmobilisation;
    }

    public CategorieFiscale getCategorieFiscale() {
        return categorieFiscale;
    }

    public void setCategorieFiscale(CategorieFiscale categorieFiscale) {
        this.categorieFiscale = categorieFiscale;
    }

    public BigDecimal getValeurTerrain() {
        return valeurTerrain;
    }

    public void setValeurTerrain(BigDecimal valeurTerrain) {
        this.valeurTerrain = valeurTerrain;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<EcritureComptable> getEcrituresComptables() {
        return ecrituresComptables;
    }
    public void setEcrituresComptables(List<EcritureComptable> ecrituresComptables) {
        this.ecrituresComptables = ecrituresComptables;
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

    public enum CategorieFiscale {
        BATIMENT_50_ANS("Bâtiment (50 ans)"),
        BATIMENT_25_ANS("Bâtiment (25 ans)"),
        MOBILIER_10_ANS("Mobilier (10 ans)"),
        MOBILIER_5_ANS("Mobilier (5 ans)"),
        TRAVAUX_10_ANS("Travaux (10 ans)"),
        TRAVAUX_5_ANS("Travaux (5 ans)"),
        EQUIPEMENT_5_ANS("Équipement (5 ans)"),
        EQUIPEMENT_3_ANS("Équipement (3 ans)"),
        TERRAIN("Terrain (non amortissable)");

        private final String libelle;

        CategorieFiscale(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        public int getDureeAnnees() {
            switch (this) {
                case BATIMENT_50_ANS: return 50;
                case BATIMENT_25_ANS: return 25;
                case MOBILIER_10_ANS: return 10;
                case MOBILIER_5_ANS: return 5;
                case TRAVAUX_10_ANS: return 10;
                case TRAVAUX_5_ANS: return 5;
                case EQUIPEMENT_5_ANS: return 5;
                case EQUIPEMENT_3_ANS: return 3;
                default: return 10;
            }
        }
    }
}