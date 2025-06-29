package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.centre.model.Immobilisation;

@Repository
public interface ImmobilisationRepository extends JpaRepository<Immobilisation, UUID> {

    @Query("SELECT i FROM Immobilisation i WHERE i.utilisateur.id = :utilisateurId ORDER BY i.creeLe DESC")
    List<Immobilisation> findByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

    @Query("SELECT i FROM Immobilisation i WHERE i.propriete.id = :proprieteId ORDER BY i.creeLe DESC")
    List<Immobilisation> findByProprieteId(@Param("proprieteId") UUID proprieteId);

    @Query("SELECT i FROM Immobilisation i WHERE i.utilisateur.id = :utilisateurId AND i.propriete.id = :proprieteId ORDER BY i.creeLe DESC")
    List<Immobilisation> findByUtilisateurIdAndProprieteId(@Param("utilisateurId") UUID utilisateurId, @Param("proprieteId") UUID proprieteId);

    @Query("SELECT i FROM Immobilisation i WHERE i.utilisateur.id = :utilisateurId AND i.typeImmobilisation = :type ORDER BY i.creeLe DESC")
    List<Immobilisation> findByUtilisateurIdAndType(@Param("utilisateurId") UUID utilisateurId, @Param("type") String type);

    @Query("SELECT i FROM Immobilisation i WHERE i.utilisateur.id = :utilisateurId AND i.categorieFiscale = :categorie ORDER BY i.creeLe DESC")
    List<Immobilisation> findByUtilisateurIdAndCategorieFiscale(@Param("utilisateurId") UUID utilisateurId, @Param("categorie") Immobilisation.CategorieFiscale categorie);
}
