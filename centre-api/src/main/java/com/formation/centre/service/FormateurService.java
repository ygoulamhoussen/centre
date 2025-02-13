package com.formation.centre.service;

import com.formation.centre.model.Formateur;
import com.formation.centre.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
