package com.formation.centre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.formation.centre.model.Formation;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    // JpaRepository provides all the necessary CRUD methods
    List<Formation> findByTitreContainingIgnoreCase(String titre);
    List<Formation> findByDescriptionContainingIgnoreCase(String description);
    List<Formation> findByTitreContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titre, String description);

    @Query("SELECT f FROM Formation f WHERE LOWER(f.titre) LIKE LOWER(CONCAT('%', :searchString, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<Formation> searchByTitreOrDescription(String searchString);
}
