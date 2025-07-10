package com.formation.centre.repository;

import com.formation.centre.model.CompteComptable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteComptableRepository extends JpaRepository<CompteComptable, Long> {
    CompteComptable findByCode(String code);
} 