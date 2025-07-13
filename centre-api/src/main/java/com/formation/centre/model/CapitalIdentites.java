package com.formation.centre.model;

import com.formation.centre.dto.CapitalIdentitesDTO;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "capital_identites")
public class CapitalIdentites {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "nom_exploitant", nullable = false)
    private String nomExploitant;
    
    @Column(name = "prenom_exploitant", nullable = false)
    private String prenomExploitant;
    
    @Column(name = "adresse_exploitant", nullable = false)
    private String adresseExploitant;
    
    @Column(name = "code_postal_exploitant", nullable = false)
    private String codePostalExploitant;
    
    @Column(name = "ville_exploitant", nullable = false)
    private String villeExploitant;
    
    @Column(name = "qualite", nullable = false)
    private String qualite;
    
    @Column(name = "apports_numeraires", precision = 15, scale = 2)
    private BigDecimal apportsNumeraires;
    
    @Column(name = "apports_nature", precision = 15, scale = 2)
    private BigDecimal apportsNature;
    
    @Column(name = "apports_industrie", precision = 15, scale = 2)
    private BigDecimal apportsIndustrie;
    
    @Column(name = "parts_detenues")
    private String partsDetenues;
    
    @Column(name = "total_capital", precision = 15, scale = 2)
    private BigDecimal totalCapital;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;
    
    @Column(name = "date_modification")
    private LocalDate dateModification;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut
    public CapitalIdentites() {}
    
    // Constructeur avec paramètres
    public CapitalIdentites(String nomExploitant, String prenomExploitant, String adresseExploitant,
                           String codePostalExploitant, String villeExploitant, String qualite,
                           BigDecimal apportsNumeraires, BigDecimal apportsNature, BigDecimal apportsIndustrie,
                           String partsDetenues, Utilisateur utilisateur) {
        this.nomExploitant = nomExploitant;
        this.prenomExploitant = prenomExploitant;
        this.adresseExploitant = adresseExploitant;
        this.codePostalExploitant = codePostalExploitant;
        this.villeExploitant = villeExploitant;
        this.qualite = qualite;
        this.apportsNumeraires = apportsNumeraires;
        this.apportsNature = apportsNature;
        this.apportsIndustrie = apportsIndustrie;
        this.partsDetenues = partsDetenues;
        this.utilisateur = utilisateur;
        this.dateCreation = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        calculerTotalCapital();
    }
    
    // Méthode pour calculer le total du capital
    public void calculerTotalCapital() {
        BigDecimal total = BigDecimal.ZERO;
        if (apportsNumeraires != null) {
            total = total.add(apportsNumeraires);
        }
        if (apportsNature != null) {
            total = total.add(apportsNature);
        }
        if (apportsIndustrie != null) {
            total = total.add(apportsIndustrie);
        }
        this.totalCapital = total;
    }
    
    // Méthode pour convertir en DTO
    public static CapitalIdentitesDTO toDTO(CapitalIdentites entity) {
        if (entity == null) return null;
        
        CapitalIdentitesDTO dto = new CapitalIdentitesDTO();
        dto.setId(entity.getId().toString());
        dto.setNomExploitant(entity.getNomExploitant());
        dto.setPrenomExploitant(entity.getPrenomExploitant());
        dto.setAdresseExploitant(entity.getAdresseExploitant());
        dto.setCodePostalExploitant(entity.getCodePostalExploitant());
        dto.setVilleExploitant(entity.getVilleExploitant());
        dto.setQualite(entity.getQualite());
        dto.setApportsNumeraires(entity.getApportsNumeraires());
        dto.setApportsNature(entity.getApportsNature());
        dto.setApportsIndustrie(entity.getApportsIndustrie());
        dto.setPartsDetenues(entity.getPartsDetenues());
        dto.setTotalCapital(entity.getTotalCapital());
        dto.setUtilisateurId(entity.getUtilisateur().getId().toString());
        dto.setDateCreation(entity.getDateCreation());
        dto.setDateModification(entity.getDateModification());
        
        return dto;
    }
    
    // Getters et Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNomExploitant() { return nomExploitant; }
    public void setNomExploitant(String nomExploitant) { this.nomExploitant = nomExploitant; }
    
    public String getPrenomExploitant() { return prenomExploitant; }
    public void setPrenomExploitant(String prenomExploitant) { this.prenomExploitant = prenomExploitant; }
    
    public String getAdresseExploitant() { return adresseExploitant; }
    public void setAdresseExploitant(String adresseExploitant) { this.adresseExploitant = adresseExploitant; }
    
    public String getCodePostalExploitant() { return codePostalExploitant; }
    public void setCodePostalExploitant(String codePostalExploitant) { this.codePostalExploitant = codePostalExploitant; }
    
    public String getVilleExploitant() { return villeExploitant; }
    public void setVilleExploitant(String villeExploitant) { this.villeExploitant = villeExploitant; }
    
    public String getQualite() { return qualite; }
    public void setQualite(String qualite) { this.qualite = qualite; }
    
    public BigDecimal getApportsNumeraires() { return apportsNumeraires; }
    public void setApportsNumeraires(BigDecimal apportsNumeraires) { 
        this.apportsNumeraires = apportsNumeraires; 
        calculerTotalCapital();
    }
    
    public BigDecimal getApportsNature() { return apportsNature; }
    public void setApportsNature(BigDecimal apportsNature) { 
        this.apportsNature = apportsNature; 
        calculerTotalCapital();
    }
    
    public BigDecimal getApportsIndustrie() { return apportsIndustrie; }
    public void setApportsIndustrie(BigDecimal apportsIndustrie) { 
        this.apportsIndustrie = apportsIndustrie; 
        calculerTotalCapital();
    }
    
    public String getPartsDetenues() { return partsDetenues; }
    public void setPartsDetenues(String partsDetenues) { this.partsDetenues = partsDetenues; }
    
    public BigDecimal getTotalCapital() { return totalCapital; }
    public void setTotalCapital(BigDecimal totalCapital) { this.totalCapital = totalCapital; }
    
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    
    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
    
    public LocalDate getDateModification() { return dateModification; }
    public void setDateModification(LocalDate dateModification) { this.dateModification = dateModification; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 