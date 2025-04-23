package com.formation.centre.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, UUID> {

}