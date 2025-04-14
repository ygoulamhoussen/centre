package com.formation.centre.repository;

import com.formation.centre.model.EcritureComptable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EcritureComptableRepository extends JpaRepository<EcritureComptable, UUID> {
}
