package com.formation.centre.repository;

import com.formation.centre.model.Propriete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProprieteRepository extends JpaRepository<Propriete, UUID> {
}
