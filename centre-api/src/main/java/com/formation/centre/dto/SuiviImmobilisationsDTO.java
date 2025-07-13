package com.formation.centre.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SuiviImmobilisationsDTO {
    private List<ImmobilisationDetailDTO> immobilisations;
    private BigDecimal totalBaseAmortissable;
    private BigDecimal totalCumulAmortissementsAnterieurs;
    private BigDecimal totalDotationExercice;
    private BigDecimal totalCumulAmortissementsCloture;
    private BigDecimal totalValeurNetteComptable;
    private int annee;

    public static class ImmobilisationDetailDTO {
        private String id;
        private String nature;
        private LocalDate dateAcquisition;
        private BigDecimal montantHT;
        private BigDecimal baseAmortissable;
        private int dureeAmortissement;
        private BigDecimal tauxAmortissement;
        private BigDecimal cumulAmortissementsAnterieurs;
        private BigDecimal dotationExercice;
        private BigDecimal amortissementExceptionnel;
        private BigDecimal cumulAmortissementsCloture;
        private BigDecimal valeurNetteComptable;
        private String propriete;

        // Constructeur
        public ImmobilisationDetailDTO() {}

        public ImmobilisationDetailDTO(String id, String nature, LocalDate dateAcquisition, 
                                     BigDecimal montantHT, BigDecimal baseAmortissable, 
                                     int dureeAmortissement, BigDecimal tauxAmortissement,
                                     BigDecimal cumulAmortissementsAnterieurs, BigDecimal dotationExercice,
                                     BigDecimal amortissementExceptionnel, BigDecimal cumulAmortissementsCloture,
                                     BigDecimal valeurNetteComptable, String propriete) {
            this.id = id;
            this.nature = nature;
            this.dateAcquisition = dateAcquisition;
            this.montantHT = montantHT;
            this.baseAmortissable = baseAmortissable;
            this.dureeAmortissement = dureeAmortissement;
            this.tauxAmortissement = tauxAmortissement;
            this.cumulAmortissementsAnterieurs = cumulAmortissementsAnterieurs;
            this.dotationExercice = dotationExercice;
            this.amortissementExceptionnel = amortissementExceptionnel;
            this.cumulAmortissementsCloture = cumulAmortissementsCloture;
            this.valeurNetteComptable = valeurNetteComptable;
            this.propriete = propriete;
        }

        // Getters et Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getNature() { return nature; }
        public void setNature(String nature) { this.nature = nature; }

        public LocalDate getDateAcquisition() { return dateAcquisition; }
        public void setDateAcquisition(LocalDate dateAcquisition) { this.dateAcquisition = dateAcquisition; }

        public BigDecimal getMontantHT() { return montantHT; }
        public void setMontantHT(BigDecimal montantHT) { this.montantHT = montantHT; }

        public BigDecimal getBaseAmortissable() { return baseAmortissable; }
        public void setBaseAmortissable(BigDecimal baseAmortissable) { this.baseAmortissable = baseAmortissable; }

        public int getDureeAmortissement() { return dureeAmortissement; }
        public void setDureeAmortissement(int dureeAmortissement) { this.dureeAmortissement = dureeAmortissement; }

        public BigDecimal getTauxAmortissement() { return tauxAmortissement; }
        public void setTauxAmortissement(BigDecimal tauxAmortissement) { this.tauxAmortissement = tauxAmortissement; }

        public BigDecimal getCumulAmortissementsAnterieurs() { return cumulAmortissementsAnterieurs; }
        public void setCumulAmortissementsAnterieurs(BigDecimal cumulAmortissementsAnterieurs) { this.cumulAmortissementsAnterieurs = cumulAmortissementsAnterieurs; }

        public BigDecimal getDotationExercice() { return dotationExercice; }
        public void setDotationExercice(BigDecimal dotationExercice) { this.dotationExercice = dotationExercice; }

        public BigDecimal getAmortissementExceptionnel() { return amortissementExceptionnel; }
        public void setAmortissementExceptionnel(BigDecimal amortissementExceptionnel) { this.amortissementExceptionnel = amortissementExceptionnel; }

        public BigDecimal getCumulAmortissementsCloture() { return cumulAmortissementsCloture; }
        public void setCumulAmortissementsCloture(BigDecimal cumulAmortissementsCloture) { this.cumulAmortissementsCloture = cumulAmortissementsCloture; }

        public BigDecimal getValeurNetteComptable() { return valeurNetteComptable; }
        public void setValeurNetteComptable(BigDecimal valeurNetteComptable) { this.valeurNetteComptable = valeurNetteComptable; }

        public String getPropriete() { return propriete; }
        public void setPropriete(String propriete) { this.propriete = propriete; }
    }

    // Constructeur
    public SuiviImmobilisationsDTO() {}

    public SuiviImmobilisationsDTO(List<ImmobilisationDetailDTO> immobilisations, int annee) {
        this.immobilisations = immobilisations;
        this.annee = annee;
        calculerTotaux();
    }

    // Méthode pour calculer les totaux
    private void calculerTotaux() {
        this.totalBaseAmortissable = immobilisations.stream()
            .map(ImmobilisationDetailDTO::getBaseAmortissable)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalCumulAmortissementsAnterieurs = immobilisations.stream()
            .map(ImmobilisationDetailDTO::getCumulAmortissementsAnterieurs)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalDotationExercice = immobilisations.stream()
            .map(ImmobilisationDetailDTO::getDotationExercice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalCumulAmortissementsCloture = immobilisations.stream()
            .map(ImmobilisationDetailDTO::getCumulAmortissementsCloture)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalValeurNetteComptable = immobilisations.stream()
            .map(ImmobilisationDetailDTO::getValeurNetteComptable)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters et Setters
    public List<ImmobilisationDetailDTO> getImmobilisations() { return immobilisations; }
    public void setImmobilisations(List<ImmobilisationDetailDTO> immobilisations) { 
        this.immobilisations = immobilisations; 
        calculerTotaux();
    }

    public BigDecimal getTotalBaseAmortissable() { return totalBaseAmortissable; }
    public void setTotalBaseAmortissable(BigDecimal totalBaseAmortissable) { this.totalBaseAmortissable = totalBaseAmortissable; }

    public BigDecimal getTotalCumulAmortissementsAnterieurs() { return totalCumulAmortissementsAnterieurs; }
    public void setTotalCumulAmortissementsAnterieurs(BigDecimal totalCumulAmortissementsAnterieurs) { this.totalCumulAmortissementsAnterieurs = totalCumulAmortissementsAnterieurs; }

    public BigDecimal getTotalDotationExercice() { return totalDotationExercice; }
    public void setTotalDotationExercice(BigDecimal totalDotationExercice) { this.totalDotationExercice = totalDotationExercice; }

    public BigDecimal getTotalCumulAmortissementsCloture() { return totalCumulAmortissementsCloture; }
    public void setTotalCumulAmortissementsCloture(BigDecimal totalCumulAmortissementsCloture) { this.totalCumulAmortissementsCloture = totalCumulAmortissementsCloture; }

    public BigDecimal getTotalValeurNetteComptable() { return totalValeurNetteComptable; }
    public void setTotalValeurNetteComptable(BigDecimal totalValeurNetteComptable) { this.totalValeurNetteComptable = totalValeurNetteComptable; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
} 