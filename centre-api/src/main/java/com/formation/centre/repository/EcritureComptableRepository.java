package com.formation.centre.repository;

import com.formation.centre.model.EcritureComptable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EcritureComptableRepository extends JpaRepository<EcritureComptable, UUID> {
    // Plus de méthodes custom, tout passe par les nouveaux champs et la relation avec LigneEcriture
    @Query("SELECT e FROM EcritureComptable e LEFT JOIN FETCH e.lignes")
    List<EcritureComptable> findAllWithLignes();
    
    /**
     * Récupérer les écritures comptables pour une année et un utilisateur donnés
     */
    @Query("SELECT e FROM EcritureComptable e LEFT JOIN FETCH e.lignes WHERE e.utilisateur.id = :utilisateurId AND YEAR(e.dateEcriture) = :annee ORDER BY e.dateEcriture")
    List<EcritureComptable> findByUtilisateurIdAndAnnee(@Param("utilisateurId") UUID utilisateurId, @Param("annee") int annee);

    @Query("SELECT e FROM EcritureComptable e WHERE e.immobilisation.id = :immobilisationId")
    List<EcritureComptable> findByImmobilisation_Id(@Param("immobilisationId") UUID immobilisationId);
}
