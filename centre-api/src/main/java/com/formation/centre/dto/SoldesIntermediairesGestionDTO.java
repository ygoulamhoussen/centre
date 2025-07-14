package com.formation.centre.dto;

import java.math.BigDecimal;
import java.util.List;

public class SoldesIntermediairesGestionDTO {
    private String utilisateurId;
    private int annee;
    
    // Soldes calculés
    private BigDecimal productionExercice;
    private BigDecimal consommationTiers;
    private BigDecimal valeurAjoutee;
    private BigDecimal chargesPersonnel;
    private BigDecimal excedentBrutExploitation;
    private BigDecimal dotationsAmortissements;
    private BigDecimal dotationsProvisions;
    private BigDecimal resultatExploitation;
    private BigDecimal chargesFinancieres;
    private BigDecimal produitsFinanciers;
    private BigDecimal resultatCourantAvantImpot;
    private BigDecimal produitsExceptionnels;
    private BigDecimal chargesExceptionnelles;
    private BigDecimal resultatExceptionnel;
    private BigDecimal impotsSurBenefices;
    private BigDecimal resultatNet;
    
    // Détails pour traçabilité
    private List<DetailSoldeDTO> detailsProduction;
    private List<DetailSoldeDTO> detailsConsommation;
    private List<DetailSoldeDTO> detailsChargesPersonnel;
    private List<DetailSoldeDTO> detailsChargesFinancieres;
    private List<DetailSoldeDTO> detailsExceptionnel;
    
    public static class DetailSoldeDTO {
        private String libelle;
        private String date;
        private BigDecimal montant;
        private String compteNum;
        private String commentaire;
        
        public DetailSoldeDTO(String libelle, String date, BigDecimal montant, String compteNum, String commentaire) {
            this.libelle = libelle;
            this.date = date;
            this.montant = montant;
            this.compteNum = compteNum;
            this.commentaire = commentaire;
        }
        
        // Getters et Setters
        public String getLibelle() { return libelle; }
        public void setLibelle(String libelle) { this.libelle = libelle; }
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public BigDecimal getMontant() { return montant; }
        public void setMontant(BigDecimal montant) { this.montant = montant; }
        
        public String getCompteNum() { return compteNum; }
        public void setCompteNum(String compteNum) { this.compteNum = compteNum; }
        
        public String getCommentaire() { return commentaire; }
        public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    }
    
    public SoldesIntermediairesGestionDTO(String utilisateurId, int annee) {
        this.utilisateurId = utilisateurId;
        this.annee = annee;
        this.productionExercice = BigDecimal.ZERO;
        this.consommationTiers = BigDecimal.ZERO;
        this.valeurAjoutee = BigDecimal.ZERO;
        this.chargesPersonnel = BigDecimal.ZERO;
        this.excedentBrutExploitation = BigDecimal.ZERO;
        this.dotationsAmortissements = BigDecimal.ZERO;
        this.dotationsProvisions = BigDecimal.ZERO;
        this.resultatExploitation = BigDecimal.ZERO;
        this.chargesFinancieres = BigDecimal.ZERO;
        this.produitsFinanciers = BigDecimal.ZERO;
        this.resultatCourantAvantImpot = BigDecimal.ZERO;
        this.produitsExceptionnels = BigDecimal.ZERO;
        this.chargesExceptionnelles = BigDecimal.ZERO;
        this.resultatExceptionnel = BigDecimal.ZERO;
        this.impotsSurBenefices = BigDecimal.ZERO;
        this.resultatNet = BigDecimal.ZERO;
    }
    
    // Méthode pour calculer tous les soldes
    public void calculerSoldes() {
        // Valeur ajoutée = Production - Consommation
        this.valeurAjoutee = this.productionExercice.subtract(this.consommationTiers);
        
        // EBE = Valeur ajoutée - Charges de personnel (souvent 0 en LMNP)
        this.excedentBrutExploitation = this.valeurAjoutee.subtract(this.chargesPersonnel);
        
        // Résultat d'exploitation = EBE - Dotations
        this.resultatExploitation = this.excedentBrutExploitation
            .subtract(this.dotationsAmortissements)
            .subtract(this.dotationsProvisions);
        
        // Résultat courant = Résultat d'exploitation ± charges/produits financiers
        this.resultatCourantAvantImpot = this.resultatExploitation
            .subtract(this.chargesFinancieres)
            .add(this.produitsFinanciers);
        
        // Résultat exceptionnel = Produits - Charges exceptionnels
        this.resultatExceptionnel = this.produitsExceptionnels.subtract(this.chargesExceptionnelles);
        
        // Résultat net = Résultat courant + exceptionnel - impôts
        this.resultatNet = this.resultatCourantAvantImpot
            .add(this.resultatExceptionnel)
            .subtract(this.impotsSurBenefices);
    }
    
    // Getters et Setters
    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    
    public BigDecimal getProductionExercice() { return productionExercice; }
    public void setProductionExercice(BigDecimal productionExercice) { this.productionExercice = productionExercice; }
    
    public BigDecimal getConsommationTiers() { return consommationTiers; }
    public void setConsommationTiers(BigDecimal consommationTiers) { this.consommationTiers = consommationTiers; }
    
    public BigDecimal getValeurAjoutee() { return valeurAjoutee; }
    public void setValeurAjoutee(BigDecimal valeurAjoutee) { this.valeurAjoutee = valeurAjoutee; }
    
    public BigDecimal getChargesPersonnel() { return chargesPersonnel; }
    public void setChargesPersonnel(BigDecimal chargesPersonnel) { this.chargesPersonnel = chargesPersonnel; }
    
    public BigDecimal getExcedentBrutExploitation() { return excedentBrutExploitation; }
    public void setExcedentBrutExploitation(BigDecimal excedentBrutExploitation) { this.excedentBrutExploitation = excedentBrutExploitation; }
    
    public BigDecimal getDotationsAmortissements() { return dotationsAmortissements; }
    public void setDotationsAmortissements(BigDecimal dotationsAmortissements) { this.dotationsAmortissements = dotationsAmortissements; }
    
    public BigDecimal getDotationsProvisions() { return dotationsProvisions; }
    public void setDotationsProvisions(BigDecimal dotationsProvisions) { this.dotationsProvisions = dotationsProvisions; }
    
    public BigDecimal getResultatExploitation() { return resultatExploitation; }
    public void setResultatExploitation(BigDecimal resultatExploitation) { this.resultatExploitation = resultatExploitation; }
    
    public BigDecimal getChargesFinancieres() { return chargesFinancieres; }
    public void setChargesFinancieres(BigDecimal chargesFinancieres) { this.chargesFinancieres = chargesFinancieres; }
    
    public BigDecimal getProduitsFinanciers() { return produitsFinanciers; }
    public void setProduitsFinanciers(BigDecimal produitsFinanciers) { this.produitsFinanciers = produitsFinanciers; }
    
    public BigDecimal getResultatCourantAvantImpot() { return resultatCourantAvantImpot; }
    public void setResultatCourantAvantImpot(BigDecimal resultatCourantAvantImpot) { this.resultatCourantAvantImpot = resultatCourantAvantImpot; }
    
    public BigDecimal getProduitsExceptionnels() { return produitsExceptionnels; }
    public void setProduitsExceptionnels(BigDecimal produitsExceptionnels) { this.produitsExceptionnels = produitsExceptionnels; }
    
    public BigDecimal getChargesExceptionnelles() { return chargesExceptionnelles; }
    public void setChargesExceptionnelles(BigDecimal chargesExceptionnelles) { this.chargesExceptionnelles = chargesExceptionnelles; }
    
    public BigDecimal getResultatExceptionnel() { return resultatExceptionnel; }
    public void setResultatExceptionnel(BigDecimal resultatExceptionnel) { this.resultatExceptionnel = resultatExceptionnel; }
    
    public BigDecimal getImpotsSurBenefices() { return impotsSurBenefices; }
    public void setImpotsSurBenefices(BigDecimal impotsSurBenefices) { this.impotsSurBenefices = impotsSurBenefices; }
    
    public BigDecimal getResultatNet() { return resultatNet; }
    public void setResultatNet(BigDecimal resultatNet) { this.resultatNet = resultatNet; }
    
    public List<DetailSoldeDTO> getDetailsProduction() { return detailsProduction; }
    public void setDetailsProduction(List<DetailSoldeDTO> detailsProduction) { this.detailsProduction = detailsProduction; }
    
    public List<DetailSoldeDTO> getDetailsConsommation() { return detailsConsommation; }
    public void setDetailsConsommation(List<DetailSoldeDTO> detailsConsommation) { this.detailsConsommation = detailsConsommation; }
    
    public List<DetailSoldeDTO> getDetailsChargesPersonnel() { return detailsChargesPersonnel; }
    public void setDetailsChargesPersonnel(List<DetailSoldeDTO> detailsChargesPersonnel) { this.detailsChargesPersonnel = detailsChargesPersonnel; }
    
    public List<DetailSoldeDTO> getDetailsChargesFinancieres() { return detailsChargesFinancieres; }
    public void setDetailsChargesFinancieres(List<DetailSoldeDTO> detailsChargesFinancieres) { this.detailsChargesFinancieres = detailsChargesFinancieres; }
    
    public List<DetailSoldeDTO> getDetailsExceptionnel() { return detailsExceptionnel; }
    public void setDetailsExceptionnel(List<DetailSoldeDTO> detailsExceptionnel) { this.detailsExceptionnel = detailsExceptionnel; }
} 