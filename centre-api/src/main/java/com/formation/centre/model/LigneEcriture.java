package com.formation.centre.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ligne_ecriture")
public class LigneEcriture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ecriture_id", nullable = false)
    private EcritureComptable ecriture;

    @Column(name = "compte_num", nullable = false, length = 20)
    private String compteNum;

    @Column(name = "compte_libelle", nullable = false, length = 255)
    private String compteLibelle;

    @Column(name = "debit", precision = 12, scale = 2)
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(name = "credit", precision = 12, scale = 2)
    private BigDecimal credit = BigDecimal.ZERO;

    @Column(name = "tiers", length = 255)
    private String tiers;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    public LigneEcriture() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public EcritureComptable getEcriture() { return ecriture; }
    public void setEcriture(EcritureComptable ecriture) { this.ecriture = ecriture; }

    public String getCompteNum() { return compteNum; }
    public void setCompteNum(String compteNum) { this.compteNum = compteNum; }

    public String getCompteLibelle() { return compteLibelle; }
    public void setCompteLibelle(String compteLibelle) { this.compteLibelle = compteLibelle; }

    public BigDecimal getDebit() { return debit; }
    public void setDebit(BigDecimal debit) { this.debit = debit; }

    public BigDecimal getCredit() { return credit; }
    public void setCredit(BigDecimal credit) { this.credit = credit; }

    public String getTiers() { return tiers; }
    public void setTiers(String tiers) { this.tiers = tiers; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
} 