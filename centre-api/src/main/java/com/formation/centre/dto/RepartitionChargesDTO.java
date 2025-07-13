package com.formation.centre.dto;

import java.math.BigDecimal;
import java.util.List;

public class RepartitionChargesDTO {
    
    private String utilisateurId;
    private int annee;
    
    // Achats (matières, fournitures, petit matériel)
    private BigDecimal achats;
    private List<ChargeDetailDTO> detailsAchats;
    
    // Autres charges externes
    private BigDecimal autresChargesExternes;
    private List<ChargeDetailDTO> detailsChargesExternes;
    
    // Impôts et taxes
    private BigDecimal impotsTaxes;
    private List<ChargeDetailDTO> detailsImpotsTaxes;
    
    // Charges de personnel (rare en LMNP)
    private BigDecimal chargesPersonnel;
    private List<ChargeDetailDTO> detailsChargesPersonnel;
    
    // Charges sociales de l'exploitant (peu courant)
    private BigDecimal chargesSociales;
    private List<ChargeDetailDTO> detailsChargesSociales;
    
    // Dotations aux amortissements
    private BigDecimal dotationsAmortissements;
    private List<ChargeDetailDTO> detailsAmortissements;
    
    // Autres charges
    private BigDecimal autresCharges;
    private List<ChargeDetailDTO> detailsAutresCharges;
    
    // Total des charges
    private BigDecimal totalCharges;
    
    public static class ChargeDetailDTO {
        private String libelle;
        private String dateCharge;
        private BigDecimal montant;
        private String commentaire;
        private String compteComptable;
        
        public ChargeDetailDTO() {}
        
        public ChargeDetailDTO(String libelle, String dateCharge, BigDecimal montant, String commentaire, String compteComptable) {
            this.libelle = libelle;
            this.dateCharge = dateCharge;
            this.montant = montant;
            this.commentaire = commentaire;
            this.compteComptable = compteComptable;
        }
        
        // Getters et Setters
        public String getLibelle() { return libelle; }
        public void setLibelle(String libelle) { this.libelle = libelle; }
        
        public String getDateCharge() { return dateCharge; }
        public void setDateCharge(String dateCharge) { this.dateCharge = dateCharge; }
        
        public BigDecimal getMontant() { return montant; }
        public void setMontant(BigDecimal montant) { this.montant = montant; }
        
        public String getCommentaire() { return commentaire; }
        public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
        
        public String getCompteComptable() { return compteComptable; }
        public void setCompteComptable(String compteComptable) { this.compteComptable = compteComptable; }
    }
    
    // Constructeur par défaut
    public RepartitionChargesDTO() {}
    
    // Constructeur avec paramètres
    public RepartitionChargesDTO(String utilisateurId, int annee) {
        this.utilisateurId = utilisateurId;
        this.annee = annee;
        this.achats = BigDecimal.ZERO;
        this.autresChargesExternes = BigDecimal.ZERO;
        this.impotsTaxes = BigDecimal.ZERO;
        this.chargesPersonnel = BigDecimal.ZERO;
        this.chargesSociales = BigDecimal.ZERO;
        this.dotationsAmortissements = BigDecimal.ZERO;
        this.autresCharges = BigDecimal.ZERO;
        this.totalCharges = BigDecimal.ZERO;
    }
    
    // Méthode pour calculer le total
    public void calculerTotal() {
        this.totalCharges = BigDecimal.ZERO
            .add(achats != null ? achats : BigDecimal.ZERO)
            .add(autresChargesExternes != null ? autresChargesExternes : BigDecimal.ZERO)
            .add(impotsTaxes != null ? impotsTaxes : BigDecimal.ZERO)
            .add(chargesPersonnel != null ? chargesPersonnel : BigDecimal.ZERO)
            .add(chargesSociales != null ? chargesSociales : BigDecimal.ZERO)
            .add(dotationsAmortissements != null ? dotationsAmortissements : BigDecimal.ZERO)
            .add(autresCharges != null ? autresCharges : BigDecimal.ZERO);
    }
    
    // Getters et Setters
    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    
    public BigDecimal getAchats() { return achats; }
    public void setAchats(BigDecimal achats) { this.achats = achats; }
    
    public List<ChargeDetailDTO> getDetailsAchats() { return detailsAchats; }
    public void setDetailsAchats(List<ChargeDetailDTO> detailsAchats) { this.detailsAchats = detailsAchats; }
    
    public BigDecimal getAutresChargesExternes() { return autresChargesExternes; }
    public void setAutresChargesExternes(BigDecimal autresChargesExternes) { this.autresChargesExternes = autresChargesExternes; }
    
    public List<ChargeDetailDTO> getDetailsChargesExternes() { return detailsChargesExternes; }
    public void setDetailsChargesExternes(List<ChargeDetailDTO> detailsChargesExternes) { this.detailsChargesExternes = detailsChargesExternes; }
    
    public BigDecimal getImpotsTaxes() { return impotsTaxes; }
    public void setImpotsTaxes(BigDecimal impotsTaxes) { this.impotsTaxes = impotsTaxes; }
    
    public List<ChargeDetailDTO> getDetailsImpotsTaxes() { return detailsImpotsTaxes; }
    public void setDetailsImpotsTaxes(List<ChargeDetailDTO> detailsImpotsTaxes) { this.detailsImpotsTaxes = detailsImpotsTaxes; }
    
    public BigDecimal getChargesPersonnel() { return chargesPersonnel; }
    public void setChargesPersonnel(BigDecimal chargesPersonnel) { this.chargesPersonnel = chargesPersonnel; }
    
    public List<ChargeDetailDTO> getDetailsChargesPersonnel() { return detailsChargesPersonnel; }
    public void setDetailsChargesPersonnel(List<ChargeDetailDTO> detailsChargesPersonnel) { this.detailsChargesPersonnel = detailsChargesPersonnel; }
    
    public BigDecimal getChargesSociales() { return chargesSociales; }
    public void setChargesSociales(BigDecimal chargesSociales) { this.chargesSociales = chargesSociales; }
    
    public List<ChargeDetailDTO> getDetailsChargesSociales() { return detailsChargesSociales; }
    public void setDetailsChargesSociales(List<ChargeDetailDTO> detailsChargesSociales) { this.detailsChargesSociales = detailsChargesSociales; }
    
    public BigDecimal getDotationsAmortissements() { return dotationsAmortissements; }
    public void setDotationsAmortissements(BigDecimal dotationsAmortissements) { this.dotationsAmortissements = dotationsAmortissements; }
    
    public List<ChargeDetailDTO> getDetailsAmortissements() { return detailsAmortissements; }
    public void setDetailsAmortissements(List<ChargeDetailDTO> detailsAmortissements) { this.detailsAmortissements = detailsAmortissements; }
    
    public BigDecimal getAutresCharges() { return autresCharges; }
    public void setAutresCharges(BigDecimal autresCharges) { this.autresCharges = autresCharges; }
    
    public List<ChargeDetailDTO> getDetailsAutresCharges() { return detailsAutresCharges; }
    public void setDetailsAutresCharges(List<ChargeDetailDTO> detailsAutresCharges) { this.detailsAutresCharges = detailsAutresCharges; }
    
    public BigDecimal getTotalCharges() { return totalCharges; }
    public void setTotalCharges(BigDecimal totalCharges) { this.totalCharges = totalCharges; }
} 