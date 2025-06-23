package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.formation.centre.model.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, UUID> {
    @Query("SELECT p FROM Paiement p " +
           "JOIN p.quittance q " +
           "JOIN q.location l " +
           "JOIN l.propriete prop " +
           "JOIN prop.utilisateur u " +
           "WHERE u.id = :utilisateurId")
    List<Paiement> findByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

    List<Paiement> findByQuittance_Location_Propriete_Utilisateur_Id(UUID utilisateurId);

    List<Paiement> findByQuittance_Id(UUID quittanceId);
}