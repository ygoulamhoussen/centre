package com.formation.centre.dto;

import java.util.List;
import com.formation.centre.dto.ResultatFiscalDetailDTOs.*;

public class ResultatFiscalDTO {
    private double recettesLocatives;
    private double chargesDeductibles;
    private double amortissements;
    private double resultatFiscal;
    private List<RecetteDetailDTO> recettesDetail;
    private List<ChargeDetailDTO> chargesDetail;
    private List<AmortissementDetailDTO> amortissementsDetail;

    public ResultatFiscalDTO() {}
    public ResultatFiscalDTO(double recettesLocatives, double chargesDeductibles, double amortissements, double resultatFiscal,
                             List<RecetteDetailDTO> recettesDetail,
                             List<ChargeDetailDTO> chargesDetail,
                             List<AmortissementDetailDTO> amortissementsDetail) {
        this.recettesLocatives = recettesLocatives;
        this.chargesDeductibles = chargesDeductibles;
        this.amortissements = amortissements;
        this.resultatFiscal = resultatFiscal;
        this.recettesDetail = recettesDetail;
        this.chargesDetail = chargesDetail;
        this.amortissementsDetail = amortissementsDetail;
    }
    public double getRecettesLocatives() { return recettesLocatives; }
    public void setRecettesLocatives(double recettesLocatives) { this.recettesLocatives = recettesLocatives; }
    public double getChargesDeductibles() { return chargesDeductibles; }
    public void setChargesDeductibles(double chargesDeductibles) { this.chargesDeductibles = chargesDeductibles; }
    public double getAmortissements() { return amortissements; }
    public void setAmortissements(double amortissements) { this.amortissements = amortissements; }
    public double getResultatFiscal() { return resultatFiscal; }
    public void setResultatFiscal(double resultatFiscal) { this.resultatFiscal = resultatFiscal; }
    public List<RecetteDetailDTO> getRecettesDetail() { return recettesDetail; }
    public void setRecettesDetail(List<RecetteDetailDTO> recettesDetail) { this.recettesDetail = recettesDetail; }
    public List<ChargeDetailDTO> getChargesDetail() { return chargesDetail; }
    public void setChargesDetail(List<ChargeDetailDTO> chargesDetail) { this.chargesDetail = chargesDetail; }
    public List<AmortissementDetailDTO> getAmortissementsDetail() { return amortissementsDetail; }
    public void setAmortissementsDetail(List<AmortissementDetailDTO> amortissementsDetail) { this.amortissementsDetail = amortissementsDetail; }
} 