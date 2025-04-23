package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.Location;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    // Toutes les locations d'un utilisateur via la propriété
    List<Location> findByPropriete_Utilisateur_Id(UUID utilisateurId);

}
