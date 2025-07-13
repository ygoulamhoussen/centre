package com.formation.centre.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CapitalIdentitesDTO {
    private String id;
    private String nomExploitant;
    private String prenomExploitant;
    private String adresseExploitant;
    private String codePostalExploitant;
    private String villeExploitant;
    private String qualite;
    private BigDecimal apportsNumeraires;
    private BigDecimal apportsNature;
    private BigDecimal apportsIndustrie;
    private String partsDetenues;
    private BigDecimal totalCapital;
    private String utilisateurId;
    private LocalDate dateCreation;
    private LocalDate dateModification;

    // Constructeur par défaut
    public CapitalIdentitesDTO() {}

    // Constructeur avec paramètres
    public CapitalIdentitesDTO(String id, String nomExploitant, String prenomExploitant, 
                              String adresseExploitant, String codePostalExploitant, String villeExploitant,
                              String qualite, BigDecimal apportsNumeraires, BigDecimal apportsNature,
                              BigDecimal apportsIndustrie, String partsDetenues, BigDecimal totalCapital,
                              String utilisateurId, LocalDate dateCreation, LocalDate dateModification) {
        this.id = id;
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
        this.totalCapital = totalCapital;
        this.utilisateurId = utilisateurId;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
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

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public LocalDate getDateModification() { return dateModification; }
    public void setDateModification(LocalDate dateModification) { this.dateModification = dateModification; }
} 