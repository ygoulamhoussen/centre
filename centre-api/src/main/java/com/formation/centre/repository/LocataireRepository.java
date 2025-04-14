package com.formation.centre.repository;

import com.formation.centre.model.Locataire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocataireRepository extends JpaRepository<Locataire, UUID> {
}
