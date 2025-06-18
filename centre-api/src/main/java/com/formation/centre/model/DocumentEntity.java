package com.formation.centre.model;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "document")
public class DocumentEntity {

    @Id
    private UUID id;
    
    @Column(name = "contenu",columnDefinition = "TEXT")
    private String contenu;
    
    @Column(name = "mime_type")
    private String mimeType;
    
    @Column(name = "nom_fichier")
    private String nomFichier;
    
    @Column(name = "taille")
    private Long taille;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriete_id")
    private Propriete propriete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locataire_id")
    private Locataire locataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "immobilisation_id")
    private Immobilisation immobilisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ecriture_id")
    private EcritureComptable ecriture;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_document")
    private TypeDocument typeDocument;

    private String titre;

    @Column(name = "url_fichier")
    private String urlFichier;

    @Column(name = "date_document")
    private LocalDate dateDocument;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @Column(name = "modifie_le")
    private LocalDateTime modifieLe;

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public Long getTaille() {
        return taille;
    }

    public void setTaille(Long taille) {
        this.taille = taille;
    }

    public enum TypeDocument {
        FACTURE, QUITTANCE, BAIL, JUSTIFICATIF, CONTRAT_DE_CREDIT, AUTRE
    }

    // Getters & Setters

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

    public Immobilisation getImmobilisation() {
        return immobilisation;
    }

    public void setImmobilisation(Immobilisation immobilisation) {
        this.immobilisation = immobilisation;
    }

    public EcritureComptable getEcriture() {
        return ecriture;
    }

    public void setEcriture(EcritureComptable ecriture) {
        this.ecriture = ecriture;
    }

    public TypeDocument getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrlFichier() {
        return urlFichier;
    }

    public void setUrlFichier(String urlFichier) {
        this.urlFichier = urlFichier;
    }

    public LocalDate getDateDocument() {
        return dateDocument;
    }

    public void setDateDocument(LocalDate dateDocument) {
        this.dateDocument = dateDocument;
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
