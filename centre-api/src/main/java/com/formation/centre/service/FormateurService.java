package com.formation.centre.service;

import com.formation.centre.model.Formateur;
import com.formation.centre.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormateurService {

    @Autowired
    private FormateurRepository formateurRepository;

    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }

    public Formateur createFormateur(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public Formateur updateFormateur(Long id, Formateur formateurDetails) {
        Formateur formateur = formateurRepository.findById(id).orElseThrow();
        formateur.setNom(formateurDetails.getNom());
        formateur.setEmail(formateurDetails.getEmail());
        return formateurRepository.save(formateur);
    }

    public void deleteFormateur(Long id) {
        formateurRepository.deleteById(id);
    }
}
