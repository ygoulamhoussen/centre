package com.formation.centre.repository;

import com.formation.centre.model.EcritureComptable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EcritureComptableRepository extends JpaRepository<EcritureComptable, UUID> {
    // Plus de méthodes custom, tout passe par les nouveaux champs et la relation avec LigneEcriture
}
