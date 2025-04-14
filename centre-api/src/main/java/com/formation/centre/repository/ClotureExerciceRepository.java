package com.formation.centre.repository;

import com.formation.centre.model.ClotureExercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClotureExerciceRepository extends JpaRepository<ClotureExercice, UUID> {
}
