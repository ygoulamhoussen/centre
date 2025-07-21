package com.formation.centre.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResultatFiscalDetailDTOs {

    public static class RecetteDetailDTO {
        private String intitule;
        private LocalDate dateRecette;
        private BigDecimal montant;
        private String proprieteNom;
        private String compteNum;

        public RecetteDetailDTO() {}
        public RecetteDetailDTO(String intitule, LocalDate dateRecette, BigDecimal montant, String proprieteNom, String compteNum) {
            this.intitule = intitule;
            this.dateRecette = dateRecette;
            this.montant = montant;
            this.proprieteNom = proprieteNom;
            this.compteNum = compteNum;
        }
        public String getIntitule() { return intitule; }
        public void setIntitule(String intitule) { this.intitule = intitule; }
        public LocalDate getDateRecette() { return dateRecette; }
        public void setDateRecette(LocalDate dateRecette) { this.dateRecette = dateRecette; }
        public BigDecimal getMontant() { return montant; }
        public void setMontant(BigDecimal montant) { this.montant = montant; }
        public String getProprieteNom() { return proprieteNom; }
        public void setProprieteNom(String proprieteNom) { this.proprieteNom = proprieteNom; }
        public String getCompteNum() { return compteNum; }
        public void setCompteNum(String compteNum) { this.compteNum = compteNum; }
    }

    public static class ChargeDetailDTO {
        private String intitule;
        private LocalDate dateCharge;
        private BigDecimal montant;
        private String proprieteNom;
        private String nature;
        private String compteNum;

        public ChargeDetailDTO() {}
        public ChargeDetailDTO(String intitule, LocalDate dateCharge, BigDecimal montant, String proprieteNom, String nature, String compteNum) {
            this.intitule = intitule;
            this.dateCharge = dateCharge;
            this.montant = montant;
            this.proprieteNom = proprieteNom;
            this.nature = nature;
            this.compteNum = compteNum;
        }
        public String getIntitule() { return intitule; }
        public void setIntitule(String intitule) { this.intitule = intitule; }
        public LocalDate getDateCharge() { return dateCharge; }
        public void setDateCharge(LocalDate dateCharge) { this.dateCharge = dateCharge; }
        public BigDecimal getMontant() { return montant; }
        public void setMontant(BigDecimal montant) { this.montant = montant; }
        public String getProprieteNom() { return proprieteNom; }
        public void setProprieteNom(String proprieteNom) { this.proprieteNom = proprieteNom; }
        public String getNature() { return nature; }
        public void setNature(String nature) { this.nature = nature; }
        public String getCompteNum() { return compteNum; }
        public void setCompteNum(String compteNum) { this.compteNum = compteNum; }
    }

    public static class AmortissementDetailDTO {
        private String immobilisationIntitule;
        private Integer annee;
        private BigDecimal montantAmortissement;
        private String proprieteNom;

        public AmortissementDetailDTO() {}
        public AmortissementDetailDTO(String immobilisationIntitule, Integer annee, BigDecimal montantAmortissement, String proprieteNom) {
            this.immobilisationIntitule = immobilisationIntitule;
            this.annee = annee;
            this.montantAmortissement = montantAmortissement;
            this.proprieteNom = proprieteNom;
        }
        public String getImmobilisationIntitule() { return immobilisationIntitule; }
        public void setImmobilisationIntitule(String immobilisationIntitule) { this.immobilisationIntitule = immobilisationIntitule; }
        public Integer getAnnee() { return annee; }
        public void setAnnee(Integer annee) { this.annee = annee; }
        public BigDecimal getMontantAmortissement() { return montantAmortissement; }
        public void setMontantAmortissement(BigDecimal montantAmortissement) { this.montantAmortissement = montantAmortissement; }
        public String getProprieteNom() { return proprieteNom; }
        public void setProprieteNom(String proprieteNom) { this.proprieteNom = proprieteNom; }
    }
} 