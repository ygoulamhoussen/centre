package com.formation.centre.service;

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
        if (searchString != null && !searchString.isEmpty()) {
            return formationRepository.searchByTitreOrDescription(searchString);
        } else {
            return formationRepository.findAll();
        }
    }
}
