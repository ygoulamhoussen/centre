package com.formation.centre.repository;

import com.formation.centre.model.EcheanceCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EcheanceCreditRepository extends JpaRepository<EcheanceCredit, UUID> {
}
