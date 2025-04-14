package com.formation.centre.repository;

import com.formation.centre.model.Quittance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuittanceRepository extends JpaRepository<Quittance, UUID> {
}
