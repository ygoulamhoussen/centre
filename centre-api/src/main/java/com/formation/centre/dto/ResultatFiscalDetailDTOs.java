package com.formation.centre.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ResultatFiscalDetailDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecetteDetailDTO {
        private String intitule;
        private LocalDate dateRecette;
        private BigDecimal montant;
        private String proprieteNom;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChargeDetailDTO {
        private String intitule;
        private LocalDate dateCharge;
        private BigDecimal montant;
        private String proprieteNom;
        private String nature;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AmortissementDetailDTO {
        private String immobilisationIntitule;
        private Integer annee;
        private BigDecimal montantAmortissement;
        private String proprieteNom;
    }
} 