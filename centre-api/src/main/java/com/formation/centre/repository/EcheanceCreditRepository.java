package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.EcheanceCredit;

public interface EcheanceCreditRepository extends JpaRepository<EcheanceCredit, UUID> {
    List<EcheanceCredit> findByCredit_Id(UUID creditId);
}
