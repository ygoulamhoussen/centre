package com.formation.centre.dto;

import java.math.BigDecimal;

public class LigneEcritureDTO {
    private String id;
    private String ecritureId;
    private String compteNum;
    private String compteLibelle;
    private BigDecimal debit;
    private BigDecimal credit;
    private String tiers;
    private String commentaire;

    public LigneEcritureDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEcritureId() { return ecritureId; }
    public void setEcritureId(String ecritureId) { this.ecritureId = ecritureId; }

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