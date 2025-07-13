package com.formation.centre.repository;

import com.formation.centre.model.CapitalIdentites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CapitalIdentitesRepository extends JpaRepository<CapitalIdentites, UUID> {
    
    /**
     * Trouve les informations de capital et identités par utilisateur
     */
    List<CapitalIdentites> findByUtilisateurId(UUID utilisateurId);
    
    /**
     * Trouve les informations de capital et identités par utilisateur (version avec String)
     */
    List<CapitalIdentites> findByUtilisateur_Id(UUID utilisateurId);
    
    /**
     * Trouve la première occurrence pour un utilisateur (pour LMNP en nom propre, il n'y en a qu'une)
     */
    Optional<CapitalIdentites> findFirstByUtilisateurId(UUID utilisateurId);
} 