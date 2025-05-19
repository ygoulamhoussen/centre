package com.formation.centre.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.formation.centre.model.DocumentEntity;
public interface DocumentEntityRepository extends JpaRepository<DocumentEntity, UUID> {

    public List<DocumentEntity> findByUtilisateur_Id(UUID userId);

    public List<DocumentEntity> findByPropriete_Id(UUID proprieteId);

    public List<DocumentEntity> findByLocataire_Id(UUID id);


}
