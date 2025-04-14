package com.formation.centre.repository;

import com.formation.centre.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaiementRepository extends JpaRepository<Paiement, UUID> {
}