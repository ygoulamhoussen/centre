package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, UUID> {
    List<Paiement> findByQuittance_Location_Propriete_Utilisateur_Id(UUID utilisateurId);

}