package com.formation.centre.repository;

import com.formation.centre.model.Immobilisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImmobilisationRepository extends JpaRepository<Immobilisation, UUID> {
}
