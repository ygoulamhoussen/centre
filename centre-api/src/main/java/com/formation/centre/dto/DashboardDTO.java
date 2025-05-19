package com.formation.centre.dto;

import java.math.BigDecimal;
import java.util.Map;

public class DashboardDTO {
    private long nombreBiens;
    private Map<String, Long> repartitionParType;
    private double tauxOccupation;
    private int nombreImpayes;
    private BigDecimal totalLoyersPerçus;
    private Map<String, BigDecimal> loyersMensuels; // ← Nouveau champ

    // Getters et Setters

    public long getNombreBiens() {
        return nombreBiens;
    }

    public void setNombreBiens(long nombreBiens) {
        this.nombreBiens = nombreBiens;
    }

    public Map<String, Long> getRepartitionParType() {
        return repartitionParType;
    }

    public void setRepartitionParType(Map<String, Long> repartitionParType) {
        this.repartitionParType = repartitionParType;
    }

    public double getTauxOccupation() {
        return tauxOccupation;
    }

    public void setTauxOccupation(double tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

    public int getNombreImpayes() {
        return nombreImpayes;
    }

    public void setNombreImpayes(int nombreImpayes) {
        this.nombreImpayes = nombreImpayes;
    }

    public BigDecimal getTotalLoyersPerçus() {
        return totalLoyersPerçus;
    }

    public void setTotalLoyersPerçus(BigDecimal totalLoyersPerçus) {
        this.totalLoyersPerçus = totalLoyersPerçus;
    }

    public Map<String, BigDecimal> getLoyersMensuels() {
        return loyersMensuels;
    }

    public void setLoyersMensuels(Map<String, BigDecimal> loyersMensuels) {
        this.loyersMensuels = loyersMensuels;
    }
}
