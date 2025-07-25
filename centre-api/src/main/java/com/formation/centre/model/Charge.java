package com.formation.centre.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.formation.centre.model.EcritureComptable;
import com.formation.centre.model.EcheanceCredit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "charges")
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "intitule", nullable = false)
    private String intitule;

    @Column(name = "montant", nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_charge", nullable = false)
    private LocalDate dateCharge;

    @ManyToOne
    @JoinColumn(name = "propriete_id", nullable = false)
    private Propriete propriete;

    @Column(name = "nature", nullable = false)
    private String nature;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @ManyToOne
    @JoinColumn(name = "ecriture_comptable_id")
    private EcritureComptable ecritureComptable;

    @ManyToOne
    @JoinColumn(name = "immobilisation_id")
    private Immobilisation immobilisation;

    @ManyToOne
    @JoinColumn(name = "echeance_credit_id")
    private EcheanceCredit echeanceCredit;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    // Constructeurs
    public Charge() {}

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

    public LocalDate getDateCharge() {
        return dateCharge;
    }

    public void setDateCharge(LocalDate dateCharge) {
        this.dateCharge = dateCharge;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public void setPropriete(Propriete propriete) {
        this.propriete = propriete;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public DocumentEntity getDocument() {
        return document;
    }

    public void setDocument(DocumentEntity document) {
        this.document = document;
    }

    public EcritureComptable getEcritureComptable() { return ecritureComptable; }
    public void setEcritureComptable(EcritureComptable ecritureComptable) { this.ecritureComptable = ecritureComptable; }

    public Immobilisation getImmobilisation() {
        return immobilisation;
    }
    public void setImmobilisation(Immobilisation immobilisation) {
        this.immobilisation = immobilisation;
    }

    public EcheanceCredit getEcheanceCredit() {
        return echeanceCredit;
    }
    public void setEcheanceCredit(EcheanceCredit echeanceCredit) {
        this.echeanceCredit = echeanceCredit;
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

    public enum NatureCharge {
        TRAVAUX("Travaux et réparations"),
        COPROPRIETE("Charges de copropriété"),
        TAXES("Taxes foncières"),
        ASSURANCE("Assurance"),
        GESTION("Gestion locative"),
        ELECTRICITE("Électricité"),
        EAU("Eau"),
        CHAUFFAGE("Chauffage"),
        ENTRETIEN("Entretien"),
        AUTRES("Autres");

        private final String libelle;

        NatureCharge(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }
} 