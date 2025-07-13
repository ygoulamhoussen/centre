package com.formation.centre.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProduitsChargesExceptionnelsDTO {
    
    private String utilisateurId;
    private int annee;
    
    // Produits exceptionnels
    private BigDecimal produitsExceptionnels;
    private List<OperationExceptionnelleDTO> detailsProduitsExceptionnels;
    
    // Charges exceptionnelles
    private BigDecimal chargesExceptionnelles;
    private List<OperationExceptionnelleDTO> detailsChargesExceptionnelles;
    
    // Plus-values à long terme
    private BigDecimal plusValuesLongTerme;
    private List<OperationExceptionnelleDTO> detailsPlusValues;
    
    // Moins-values à long terme
    private BigDecimal moinsValuesLongTerme;
    private List<OperationExceptionnelleDTO> detailsMoinsValues;
    
    // Total des opérations exceptionnelles
    private BigDecimal totalOperationsExceptionnelles;
    
    public static class OperationExceptionnelleDTO {
        private String libelle;
        private String dateOperation;
        private BigDecimal montant;
        private String commentaire;
        private String compteComptable;
        private String typeOperation; // "PRODUIT", "CHARGE", "PLUS_VALUE", "MOINS_VALUE"
        
        public OperationExceptionnelleDTO() {}
        
        public OperationExceptionnelleDTO(String libelle, String dateOperation, BigDecimal montant, 
                                        String commentaire, String compteComptable, String typeOperation) {
            this.libelle = libelle;
            this.dateOperation = dateOperation;
            this.montant = montant;
            this.commentaire = commentaire;
            this.compteComptable = compteComptable;
            this.typeOperation = typeOperation;
        }
        
        // Getters et Setters
        public String getLibelle() { return libelle; }
        public void setLibelle(String libelle) { this.libelle = libelle; }
        
        public String getDateOperation() { return dateOperation; }
        public void setDateOperation(String dateOperation) { this.dateOperation = dateOperation; }
        
        public BigDecimal getMontant() { return montant; }
        public void setMontant(BigDecimal montant) { this.montant = montant; }
        
        public String getCommentaire() { return commentaire; }
        public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
        
        public String getCompteComptable() { return compteComptable; }
        public void setCompteComptable(String compteComptable) { this.compteComptable = compteComptable; }
        
        public String getTypeOperation() { return typeOperation; }
        public void setTypeOperation(String typeOperation) { this.typeOperation = typeOperation; }
    }
    
    // Constructeur par défaut
    public ProduitsChargesExceptionnelsDTO() {}
    
    // Constructeur avec paramètres
    public ProduitsChargesExceptionnelsDTO(String utilisateurId, int annee) {
        this.utilisateurId = utilisateurId;
        this.annee = annee;
        this.produitsExceptionnels = BigDecimal.ZERO;
        this.chargesExceptionnelles = BigDecimal.ZERO;
        this.plusValuesLongTerme = BigDecimal.ZERO;
        this.moinsValuesLongTerme = BigDecimal.ZERO;
        this.totalOperationsExceptionnelles = BigDecimal.ZERO;
    }
    
    // Méthode pour calculer le total
    public void calculerTotal() {
        this.totalOperationsExceptionnelles = BigDecimal.ZERO
            .add(produitsExceptionnels != null ? produitsExceptionnels : BigDecimal.ZERO)
            .subtract(chargesExceptionnelles != null ? chargesExceptionnelles : BigDecimal.ZERO)
            .add(plusValuesLongTerme != null ? plusValuesLongTerme : BigDecimal.ZERO)
            .subtract(moinsValuesLongTerme != null ? moinsValuesLongTerme : BigDecimal.ZERO);
    }
    
    // Getters et Setters
    public String getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(String utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    
    public BigDecimal getProduitsExceptionnels() { return produitsExceptionnels; }
    public void setProduitsExceptionnels(BigDecimal produitsExceptionnels) { this.produitsExceptionnels = produitsExceptionnels; }
    
    public List<OperationExceptionnelleDTO> getDetailsProduitsExceptionnels() { return detailsProduitsExceptionnels; }
    public void setDetailsProduitsExceptionnels(List<OperationExceptionnelleDTO> detailsProduitsExceptionnels) { this.detailsProduitsExceptionnels = detailsProduitsExceptionnels; }
    
    public BigDecimal getChargesExceptionnelles() { return chargesExceptionnelles; }
    public void setChargesExceptionnelles(BigDecimal chargesExceptionnelles) { this.chargesExceptionnelles = chargesExceptionnelles; }
    
    public List<OperationExceptionnelleDTO> getDetailsChargesExceptionnelles() { return detailsChargesExceptionnelles; }
    public void setDetailsChargesExceptionnelles(List<OperationExceptionnelleDTO> detailsChargesExceptionnelles) { this.detailsChargesExceptionnelles = detailsChargesExceptionnelles; }
    
    public BigDecimal getPlusValuesLongTerme() { return plusValuesLongTerme; }
    public void setPlusValuesLongTerme(BigDecimal plusValuesLongTerme) { this.plusValuesLongTerme = plusValuesLongTerme; }
    
    public List<OperationExceptionnelleDTO> getDetailsPlusValues() { return detailsPlusValues; }
    public void setDetailsPlusValues(List<OperationExceptionnelleDTO> detailsPlusValues) { this.detailsPlusValues = detailsPlusValues; }
    
    public BigDecimal getMoinsValuesLongTerme() { return moinsValuesLongTerme; }
    public void setMoinsValuesLongTerme(BigDecimal moinsValuesLongTerme) { this.moinsValuesLongTerme = moinsValuesLongTerme; }
    
    public List<OperationExceptionnelleDTO> getDetailsMoinsValues() { return detailsMoinsValues; }
    public void setDetailsMoinsValues(List<OperationExceptionnelleDTO> detailsMoinsValues) { this.detailsMoinsValues = detailsMoinsValues; }
    
    public BigDecimal getTotalOperationsExceptionnelles() { return totalOperationsExceptionnelles; }
    public void setTotalOperationsExceptionnelles(BigDecimal totalOperationsExceptionnelles) { this.totalOperationsExceptionnelles = totalOperationsExceptionnelles; }
} 