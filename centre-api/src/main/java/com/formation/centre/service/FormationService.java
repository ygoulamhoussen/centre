package com.formation.centre.service;

import com.formation.centre.exception.FormationNotFoundException;
import com.formation.centre.model.Formation;
import com.formation.centre.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormationService {

    @Autowired
    private FormationRepository formationRepository;

    public List<Formation> searchFormations(String searchString) {
        try {
            if (searchString != null && !searchString.isEmpty()) {
                return formationRepository.searchByTitreOrDescription(searchString);
            } else {
                return formationRepository.findAll();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error searching formations", e);
        }
    }

    public Formation getFormationById(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new FormationNotFoundException("Formation not found with id " + id));
    }
}
