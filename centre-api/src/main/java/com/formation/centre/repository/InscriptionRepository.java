package com.formation.centre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.formation.centre.model.Inscription;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    // JpaRepository provides all the necessary CRUD methods
}
