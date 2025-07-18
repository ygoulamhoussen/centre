package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "echeance_credit")
public class EcheanceCredit {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Column(name = "date_echeance")
    private LocalDate dateEcheance;

    private BigDecimal interet;

    @Column(name = "capital_rembourse")
    private BigDecimal capitalRembourse;

    private BigDecimal assurance;

    @Column(name = "total_echeance")
    private BigDecimal totalEcheance;

    @OneToMany(mappedBy = "echeanceCredit")
    private List<EcritureComptable> ecrituresComptables;

    // Getters & Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public BigDecimal getInteret() {
        return interet;
    }

    public void setInteret(BigDecimal interet) {
        this.interet = interet;
    }

    public BigDecimal getCapitalRembourse() {
        return capitalRembourse;
    }

    public void setCapitalRembourse(BigDecimal capitalRembourse) {
        this.capitalRembourse = capitalRembourse;
    }

    public BigDecimal getAssurance() {
        return assurance;
    }

    public void setAssurance(BigDecimal assurance) {
        this.assurance = assurance;
    }

    public BigDecimal getTotalEcheance() {
        return totalEcheance;
    }

    public void setTotalEcheance(BigDecimal totalEcheance) {
        this.totalEcheance = totalEcheance;
    }

    public List<EcritureComptable> getEcrituresComptables() {
        return ecrituresComptables;
    }
    public void setEcrituresComptables(List<EcritureComptable> ecrituresComptables) {
        this.ecrituresComptables = ecrituresComptables;
    }
}
