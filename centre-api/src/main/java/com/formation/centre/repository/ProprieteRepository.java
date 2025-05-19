package com.formation.centre.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.formation.centre.model.CompositionAcquisition;
import com.formation.centre.model.Propriete;

public interface ProprieteRepository extends JpaRepository<Propriete, UUID> {

    @Query("SELECT c FROM CompositionAcquisition c WHERE c.id = :id")
    Optional<CompositionAcquisition> findCompositionById(UUID id);

    @Query("SELECT c FROM CompositionAcquisition c WHERE c.id = :id")
    default CompositionAcquisition saveComposition(CompositionAcquisition composition) {
        return composition;
    }

    public List<Propriete> findByUtilisateur_Id(UUID uid);
}
