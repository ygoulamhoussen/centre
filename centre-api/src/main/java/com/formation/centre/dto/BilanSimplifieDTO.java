package com.formation.centre.dto;

import java.util.List;
import com.formation.centre.dto.BilanSimplifieDetailDTOs.*;

public class BilanSimplifieDTO {
    private List<ImmobilisationDetailDTO> immobilisationsDetail;
    private List<EmpruntDetailDTO> empruntsDetail;
    private List<TresorerieDetailDTO> tresorerieDetail;
    private double creancesClients;
    private double capital;
    private double resultatExercice;
    private double dettesFournisseurs;

    public BilanSimplifieDTO() {}

    public BilanSimplifieDTO(List<ImmobilisationDetailDTO> immobilisationsDetail,
                            List<EmpruntDetailDTO> empruntsDetail,
                            List<TresorerieDetailDTO> tresorerieDetail,
                            double creancesClients,
                            double capital,
                            double resultatExercice,
                            double dettesFournisseurs) {
        this.immobilisationsDetail = immobilisationsDetail;
        this.empruntsDetail = empruntsDetail;
        this.tresorerieDetail = tresorerieDetail;
        this.creancesClients = creancesClients;
        this.capital = capital;
        this.resultatExercice = resultatExercice;
        this.dettesFournisseurs = dettesFournisseurs;
    }

    // Getters and Setters
    public List<ImmobilisationDetailDTO> getImmobilisationsDetail() { return immobilisationsDetail; }
    public void setImmobilisationsDetail(List<ImmobilisationDetailDTO> immobilisationsDetail) { this.immobilisationsDetail = immobilisationsDetail; }

    public List<EmpruntDetailDTO> getEmpruntsDetail() { return empruntsDetail; }
    public void setEmpruntsDetail(List<EmpruntDetailDTO> empruntsDetail) { this.empruntsDetail = empruntsDetail; }

    public List<TresorerieDetailDTO> getTresorerieDetail() { return tresorerieDetail; }
    public void setTresorerieDetail(List<TresorerieDetailDTO> tresorerieDetail) { this.tresorerieDetail = tresorerieDetail; }

    public double getCreancesClients() { return creancesClients; }
    public void setCreancesClients(double creancesClients) { this.creancesClients = creancesClients; }

    public double getCapital() { return capital; }
    public void setCapital(double capital) { this.capital = capital; }

    public double getResultatExercice() { return resultatExercice; }
    public void setResultatExercice(double resultatExercice) { this.resultatExercice = resultatExercice; }

    public double getDettesFournisseurs() { return dettesFournisseurs; }
    public void setDettesFournisseurs(double dettesFournisseurs) { this.dettesFournisseurs = dettesFournisseurs; }
} 