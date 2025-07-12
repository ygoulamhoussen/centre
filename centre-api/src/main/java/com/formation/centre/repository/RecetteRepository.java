package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.centre.model.Recette;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, UUID> {

    /**
     * Récupérer toutes les recettes d'un utilisateur
     */
    @Query("SELECT r FROM Recette r WHERE r.utilisateur.id = :utilisateurId ORDER BY r.dateRecette DESC")
    List<Recette> findByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

    /**
     * Récupérer toutes les recettes d'une propriété
     */
    @Query("SELECT r FROM Recette r WHERE r.propriete.id = :proprieteId ORDER BY r.dateRecette DESC")
    List<Recette> findByProprieteId(@Param("proprieteId") UUID proprieteId);

    /**
     * Récupérer les recettes d'une propriété pour une année fiscale donnée
     */
    @Query("SELECT r FROM Recette r WHERE r.propriete.id = :proprieteId AND YEAR(r.dateRecette) = :annee ORDER BY r.dateRecette DESC")
    List<Recette> findByProprieteIdAndAnnee(@Param("proprieteId") UUID proprieteId, @Param("annee") int annee);

    /**
     * Récupérer les recettes d'un utilisateur pour une année fiscale donnée
     */
    @Query("SELECT r FROM Recette r WHERE r.utilisateur.id = :utilisateurId AND YEAR(r.dateRecette) = :annee ORDER BY r.dateRecette DESC")
    List<Recette> findByUtilisateurIdAndAnnee(@Param("utilisateurId") UUID utilisateurId, @Param("annee") int annee);

    /**
     * Récupérer les recettes par type
     */
    @Query("SELECT r FROM Recette r WHERE r.utilisateur.id = :utilisateurId AND r.type = :type ORDER BY r.dateRecette DESC")
    List<Recette> findByUtilisateurIdAndType(@Param("utilisateurId") UUID utilisateurId, @Param("type") String type);

    /**
     * Récupérer les recettes liées à une quittance
     */
    @Query("SELECT r FROM Recette r WHERE r.quittance.id = :quittanceId")
    List<Recette> findByQuittanceId(@Param("quittanceId") UUID quittanceId);

    /**
     * Sommer les recettes pour une année et une liste de propriétés données.
     */
    @Query("SELECT COALESCE(SUM(r.montant), 0) FROM Recette r WHERE r.utilisateur.id = :utilisateurId AND YEAR(r.dateRecette) = :annee AND r.propriete.id IN :proprieteIds")
    Double sumRecettesByYearAndProprietes(
        @Param("utilisateurId") UUID utilisateurId,
        @Param("annee") int annee,
        @Param("proprieteIds") List<UUID> proprieteIds
    );

    /**
     * Récupérer les recettes pour une année et une liste de propriétés données.
     */
    @Query("SELECT r FROM Recette r WHERE r.utilisateur.id = :utilisateurId AND YEAR(r.dateRecette) = :annee AND r.propriete.id IN :proprieteIds ORDER BY r.dateRecette DESC")
    List<Recette> findByYearAndProprietes(
        @Param("utilisateurId") UUID utilisateurId,
        @Param("annee") int annee,
        @Param("proprieteIds") List<UUID> proprieteIds
    );

    @Query("SELECT r FROM Recette r WHERE r.ecritureComptable.id = :ecritureId")
    java.util.Optional<Recette> findByEcritureComptable_Id(@Param("ecritureId") UUID ecritureId);
} 