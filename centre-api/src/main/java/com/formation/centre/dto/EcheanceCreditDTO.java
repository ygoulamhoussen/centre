package com.formation.centre.dto;

public class EcheanceCreditDTO {
    private String id;
    private String creditId;
    private String dateEcheance;
    private String interet;
    private String capitalRembourse;
    private String assurance;
    private String totalEcheance;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getInteret() {
        return interet;
    }

    public void setInteret(String interet) {
        this.interet = interet;
    }

    public String getCapitalRembourse() {
        return capitalRembourse;
    }

    public void setCapitalRembourse(String capitalRembourse) {
        this.capitalRembourse = capitalRembourse;
    }

    public String getAssurance() {
        return assurance;
    }

    public void setAssurance(String assurance) {
        this.assurance = assurance;
    }

    public String getTotalEcheance() {
        return totalEcheance;
    }

    public void setTotalEcheance(String totalEcheance) {
        this.totalEcheance = totalEcheance;
    }
}