package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.formation.centre.model.Location;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    // Toutes les locations d'un utilisateur via la propriété
    List<Location> findByPropriete_Utilisateur_Id(UUID utilisateurId);
    
    // Vérifier s'il existe des locations pour une propriété
    @Query("SELECT COUNT(l) > 0 FROM Location l WHERE l.propriete.id = :proprieteId")
    boolean existsByProprieteId(@Param("proprieteId") UUID proprieteId);
    
    // Récupérer les locations d'une propriété
    @Query("SELECT l FROM Location l WHERE l.propriete.id = :proprieteId")
    List<Location> findByProprieteId(@Param("proprieteId") UUID proprieteId);
}
