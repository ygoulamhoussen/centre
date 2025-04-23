package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.Quittance;

public interface QuittanceRepository extends JpaRepository<Quittance, UUID> {
    
    List<Quittance> findByLocation_Propriete_Utilisateur_Id(UUID utilisateurId);
}
