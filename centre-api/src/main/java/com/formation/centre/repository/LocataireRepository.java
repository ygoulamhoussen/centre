
package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.Locataire;

public interface LocataireRepository extends JpaRepository<Locataire, UUID> {
    List<Locataire> findByUtilisateur_Id(UUID utilisateurId);

}
