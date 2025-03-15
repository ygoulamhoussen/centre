package com.formation.centre.service;

import com.formation.centre.dto.FormationDTO;
import com.formation.centre.model.Formation;
import com.formation.centre.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    public Optional<Formation> getFormationById(Long id) {
        return formationRepository.findById(id);
    }
    
    // --- New DTO methods ---
    public List<FormationDTO> getAllFormationsDTO() {
        return formationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public FormationDTO createFormationDTO(FormationDTO dto) {
        Formation formation = convertToEntity(dto);
        Formation saved = formationRepository.save(formation);
        return convertToDTO(saved);
    }
    
    public Optional<FormationDTO> updateFormationDTO(Long id, FormationDTO dto) {
        return formationRepository.findById(id).map(existing -> {
            existing.setTitre(dto.getTitre());
            existing.setDescription(dto.getDescription());
            Formation updated = formationRepository.save(existing);
            return convertToDTO(updated);
        });
    }
    
    // --- Conversion methods ---
    private FormationDTO convertToDTO(Formation f) {
        return new FormationDTO(f.getId(), f.getTitre(), f.getDescription());
    }
    
    private Formation convertToEntity(FormationDTO dto) {
        Formation f = new Formation();
        f.setTitre(dto.getTitre());
        f.setDescription(dto.getDescription());
        return f;
    }
}
