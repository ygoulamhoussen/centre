package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.centre.model.Charge;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, UUID> {

    /**
     * Récupérer toutes les charges d'un utilisateur
     */
    @Query("SELECT c FROM Charge c WHERE c.utilisateur.id = :utilisateurId ORDER BY c.dateCharge DESC")
    List<Charge> findByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

    /**
     * Récupérer toutes les charges d'une propriété
     */
    @Query("SELECT c FROM Charge c WHERE c.propriete.id = :proprieteId ORDER BY c.dateCharge DESC")
    List<Charge> findByProprieteId(@Param("proprieteId") UUID proprieteId);

    /**
     * Récupérer les charges d'une propriété pour une année fiscale donnée
     */
    @Query("SELECT c FROM Charge c WHERE c.propriete.id = :proprieteId AND YEAR(c.dateCharge) = :annee ORDER BY c.dateCharge DESC")
    List<Charge> findByProprieteIdAndAnnee(@Param("proprieteId") UUID proprieteId, @Param("annee") int annee);

    /**
     * Récupérer les charges d'un utilisateur pour une année fiscale donnée
     */
    @Query("SELECT c FROM Charge c WHERE c.utilisateur.id = :utilisateurId AND YEAR(c.dateCharge) = :annee ORDER BY c.dateCharge DESC")
    List<Charge> findByUtilisateurIdAndAnnee(@Param("utilisateurId") UUID utilisateurId, @Param("annee") int annee);

    /**
     * Récupérer les charges par nature
     */
    @Query("SELECT c FROM Charge c WHERE c.utilisateur.id = :utilisateurId AND c.nature = :nature ORDER BY c.dateCharge DESC")
    List<Charge> findByUtilisateurIdAndNature(@Param("utilisateurId") UUID utilisateurId, @Param("nature") Charge.NatureCharge nature);
} 