package com.formation.centre.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BilanSimplifieDetailDTOs {

    public static class ImmobilisationDetailDTO {
        private String intitule;
        private String proprieteNom;
        private BigDecimal montantBrut;
        private BigDecimal amortissementsCumules;
        private BigDecimal valeurNette;
        private String typeImmobilisation;

        public ImmobilisationDetailDTO() {}
        
        public ImmobilisationDetailDTO(String intitule, String proprieteNom, BigDecimal montantBrut, 
                                      BigDecimal amortissementsCumules, BigDecimal valeurNette, String typeImmobilisation) {
            this.intitule = intitule;
            this.proprieteNom = proprieteNom;
            this.montantBrut = montantBrut;
            this.amortissementsCumules = amortissementsCumules;
            this.valeurNette = valeurNette;
            this.typeImmobilisation = typeImmobilisation;
        }

        // Getters and Setters
        public String getIntitule() { return intitule; }
        public void setIntitule(String intitule) { this.intitule = intitule; }

        public String getProprieteNom() { return proprieteNom; }
        public void setProprieteNom(String proprieteNom) { this.proprieteNom = proprieteNom; }

        public BigDecimal getMontantBrut() { return montantBrut; }
        public void setMontantBrut(BigDecimal montantBrut) { this.montantBrut = montantBrut; }

        public BigDecimal getAmortissementsCumules() { return amortissementsCumules; }
        public void setAmortissementsCumules(BigDecimal amortissementsCumules) { this.amortissementsCumules = amortissementsCumules; }

        public BigDecimal getValeurNette() { return valeurNette; }
        public void setValeurNette(BigDecimal valeurNette) { this.valeurNette = valeurNette; }

        public String getTypeImmobilisation() { return typeImmobilisation; }
        public void setTypeImmobilisation(String typeImmobilisation) { this.typeImmobilisation = typeImmobilisation; }
    }

    public static class EmpruntDetailDTO {
        private String intitule;
        private String proprieteNom;
        private BigDecimal capitalRestantDu;
        private String banque;
        private LocalDate dateFin;

        public EmpruntDetailDTO() {}
        
        public EmpruntDetailDTO(String intitule, String proprieteNom, BigDecimal capitalRestantDu, String banque, LocalDate dateFin) {
            this.intitule = intitule;
            this.proprieteNom = proprieteNom;
            this.capitalRestantDu = capitalRestantDu;
            this.banque = banque;
            this.dateFin = dateFin;
        }

        // Getters and Setters
        public String getIntitule() { return intitule; }
        public void setIntitule(String intitule) { this.intitule = intitule; }

        public String getProprieteNom() { return proprieteNom; }
        public void setProprieteNom(String proprieteNom) { this.proprieteNom = proprieteNom; }

        public BigDecimal getCapitalRestantDu() { return capitalRestantDu; }
        public void setCapitalRestantDu(BigDecimal capitalRestantDu) { this.capitalRestantDu = capitalRestantDu; }

        public String getBanque() { return banque; }
        public void setBanque(String banque) { this.banque = banque; }

        public LocalDate getDateFin() { return dateFin; }
        public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    }

    public static class TresorerieDetailDTO {
        private String compte;
        private BigDecimal solde;
        private LocalDate dateSolde;

        public TresorerieDetailDTO() {}
        
        public TresorerieDetailDTO(String compte, BigDecimal solde, LocalDate dateSolde) {
            this.compte = compte;
            this.solde = solde;
            this.dateSolde = dateSolde;
        }

        // Getters and Setters
        public String getCompte() { return compte; }
        public void setCompte(String compte) { this.compte = compte; }

        public BigDecimal getSolde() { return solde; }
        public void setSolde(BigDecimal solde) { this.solde = solde; }

        public LocalDate getDateSolde() { return dateSolde; }
        public void setDateSolde(LocalDate dateSolde) { this.dateSolde = dateSolde; }
    }
} 