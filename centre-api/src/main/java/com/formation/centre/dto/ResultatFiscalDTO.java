package com.formation.centre.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.formation.centre.dto.ResultatFiscalDetailDTOs.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultatFiscalDTO {
    private double recettesLocatives;
    private double chargesDeductibles;
    private double amortissements;
    private double resultatFiscal;
    private List<RecetteDetailDTO> recettesDetail;
    private List<ChargeDetailDTO> chargesDetail;
    private List<AmortissementDetailDTO> amortissementsDetail;
} 