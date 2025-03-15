package com.formation.centre.service;

import com.formation.centre.dto.FormateurDTO;
import com.formation.centre.model.Formateur;
import com.formation.centre.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormateurService {

    @Autowired
    private FormateurRepository formateurRepository;

    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }

    public Optional<Formateur> getFormateurById(Long id) {
        return formateurRepository.findById(id);
    }

    public Formateur createFormateur(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public Optional<Formateur> updateFormateur(Long id, Formateur formateur) {
        return formateurRepository.findById(id).map(existingFormateur -> {
            existingFormateur.setNom(formateur.getNom());
            existingFormateur.setEmail(formateur.getEmail());
            return formateurRepository.save(existingFormateur);
        });
    }

    public void deleteFormateur(Long id) {
        formateurRepository.deleteById(id);
    }

    // --- New DTO methods ---
    public List<FormateurDTO> getAllFormateursDTO() {
        return formateurRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public FormateurDTO createFormateurDTO(FormateurDTO dto) {
        Formateur formateur = convertToEntity(dto);
        Formateur saved = formateurRepository.save(formateur);
        return convertToDTO(saved);
    }

    public Optional<FormateurDTO> updateFormateurDTO(Long id, FormateurDTO dto) {
        return formateurRepository.findById(id).map(existing -> {
            existing.setNom(dto.getNom());
            existing.setEmail(dto.getEmail());
            Formateur updated = formateurRepository.save(existing);
            return convertToDTO(updated);
        });
    }

    // --- Conversion methods ---
    private FormateurDTO convertToDTO(Formateur f) {
        return new FormateurDTO(f.getId(), f.getNom(), f.getEmail());
    }

    private Formateur convertToEntity(FormateurDTO dto) {
        Formateur f = new Formateur();
        f.setNom(dto.getNom());
        f.setEmail(dto.getEmail());
        return f;
    }
}
