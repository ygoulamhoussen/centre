package com.formation.centre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.formation.centre.model.Formation;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    // JpaRepository provides all the necessary CRUD methods
}
