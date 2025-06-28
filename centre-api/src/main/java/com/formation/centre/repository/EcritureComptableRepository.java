package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.centre.model.EcritureComptable;

@Repository
public interface EcritureComptableRepository extends JpaRepository<EcritureComptable, UUID> {

    /**
     * Récupérer toutes les écritures comptables d'une propriété
     */
    @Query("SELECT e FROM EcritureComptable e WHERE e.propriete.id = :proprieteId ORDER BY e.dateEcriture DESC")
    List<EcritureComptable> findByProprieteId(@Param("proprieteId") UUID proprieteId);

    /**
     * Récupérer les écritures comptables d'une propriété pour une année fiscale donnée
     */
    @Query("SELECT e FROM EcritureComptable e WHERE e.propriete.id = :proprieteId AND YEAR(e.dateEcriture) = :annee ORDER BY e.dateEcriture DESC")
    List<EcritureComptable> findByProprieteIdAndAnnee(@Param("proprieteId") UUID proprieteId, @Param("annee") int annee);

    /**
     * Récupérer toutes les écritures comptables d'un utilisateur
     */
    @Query("SELECT e FROM EcritureComptable e WHERE e.utilisateur.id = :utilisateurId ORDER BY e.dateEcriture DESC")
    List<EcritureComptable> findByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

    /**
     * Récupérer les écritures comptables d'un utilisateur pour une année fiscale donnée
     */
    @Query("SELECT e FROM EcritureComptable e WHERE e.utilisateur.id = :utilisateurId AND YEAR(e.dateEcriture) = :annee ORDER BY e.dateEcriture DESC")
    List<EcritureComptable> findByUtilisateurIdAndAnnee(@Param("utilisateurId") UUID utilisateurId, @Param("annee") int annee);

    /**
     * Récupérer les écritures comptables par type
     */
    @Query("SELECT e FROM EcritureComptable e WHERE e.propriete.id = :proprieteId AND e.type = :type ORDER BY e.dateEcriture DESC")
    List<EcritureComptable> findByProprieteIdAndType(@Param("proprieteId") UUID proprieteId, @Param("type") EcritureComptable.TypeEcriture type);

    /**
     * Récupérer l'écriture comptable liée à une charge
     */
    @Query("SELECT e FROM EcritureComptable e WHERE e.charge.id = :chargeId")
    EcritureComptable findByChargeId(@Param("chargeId") UUID chargeId);

    /**
     * Récupérer l'écriture comptable liée à une recette
     */
    @Query("SELECT e FROM EcritureComptable e WHERE e.recette.id = :recetteId")
    EcritureComptable findByRecetteId(@Param("recetteId") UUID recetteId);
}
