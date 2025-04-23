package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.Credit;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
    List<Credit> findByPropriete_Utilisateur_Id(UUID utilisateurId);
}

