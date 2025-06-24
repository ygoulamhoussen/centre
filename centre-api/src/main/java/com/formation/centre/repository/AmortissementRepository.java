package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.Amortissement;

public interface AmortissementRepository extends JpaRepository<Amortissement, UUID> {
    List<Amortissement> findByPropriete_Id(UUID proprieteId);
}
