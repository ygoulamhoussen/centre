package com.formation.centre.repository;

import com.formation.centre.model.Amortissement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AmortissementRepository extends JpaRepository<Amortissement, UUID> {
}
