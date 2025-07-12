package com.formation.centre.repository;

import com.formation.centre.model.LigneEcriture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LigneEcritureRepository extends JpaRepository<LigneEcriture, UUID> {
    List<LigneEcriture> findByEcritureId(UUID ecritureId);
    
    /**
     * Supprimer toutes les lignes d'écriture d'une écriture comptable
     */
    void deleteByEcritureId(UUID ecritureId);
} 