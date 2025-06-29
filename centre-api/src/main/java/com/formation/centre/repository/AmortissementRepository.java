package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.formation.centre.model.Amortissement;

@Repository
public interface AmortissementRepository extends JpaRepository<Amortissement, UUID> {

    @Query("SELECT a FROM Amortissement a WHERE a.immobilisation.id = :immobilisationId ORDER BY a.annee ASC")
    List<Amortissement> findByImmobilisationId(@Param("immobilisationId") UUID immobilisationId);

    @Query("SELECT a FROM Amortissement a WHERE a.immobilisation.utilisateur.id = :utilisateurId ORDER BY a.immobilisation.intitule ASC, a.annee ASC")
    List<Amortissement> findByUtilisateurId(@Param("utilisateurId") UUID utilisateurId);

    @Query("SELECT a FROM Amortissement a WHERE a.immobilisation.propriete.id = :proprieteId ORDER BY a.immobilisation.intitule ASC, a.annee ASC")
    List<Amortissement> findByProprieteId(@Param("proprieteId") UUID proprieteId);

    @Query("SELECT a FROM Amortissement a WHERE a.immobilisation.utilisateur.id = :utilisateurId AND a.immobilisation.propriete.id = :proprieteId ORDER BY a.immobilisation.intitule ASC, a.annee ASC")
    List<Amortissement> findByUtilisateurIdAndProprieteId(@Param("utilisateurId") UUID utilisateurId, @Param("proprieteId") UUID proprieteId);

    @Query("SELECT a FROM Amortissement a WHERE a.immobilisation.id = :immobilisationId AND a.annee = :annee")
    Amortissement findByImmobilisationIdAndAnnee(@Param("immobilisationId") UUID immobilisationId, @Param("annee") Integer annee);

    @Query("SELECT a FROM Amortissement a WHERE a.immobilisation.utilisateur.id = :utilisateurId AND a.annee = :annee ORDER BY a.immobilisation.intitule ASC")
    List<Amortissement> findByUtilisateurIdAndAnnee(@Param("utilisateurId") UUID utilisateurId, @Param("annee") Integer annee);
}
