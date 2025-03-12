package com.formation.centre.controller;

import com.formation.centre.model.Formateur;
import com.formation.centre.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/formateurs")
public class FormateurController {

    @Autowired
    private FormateurRepository formateurRepository;

    @GetMapping
    public ResponseEntity<List<Formateur>> getAllFormateurs() {
        try {
            List<Formateur> formateurs = formateurRepository.findAll();
            return new ResponseEntity<>(formateurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/creer")
    public ResponseEntity<Formateur> createFormateur(@RequestBody Formateur formateur) {
        // Validate required fields
        if (formateur.getNom() == null || formateur.getNom().trim().isEmpty() ||
            formateur.getEmail() == null || formateur.getEmail().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Formateur savedFormateur = formateurRepository.save(formateur);
            return new ResponseEntity<>(savedFormateur, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Formateur> updateFormateur(@PathVariable("id") long id, @RequestBody Formateur formateur) {
        try {
            Optional<Formateur> formateurData = formateurRepository.findById(id);
            if (formateurData.isPresent()) {
                Formateur _formateur = formateurData.get();
                // Update relevant fields – adjust as necessary
                _formateur.setNom(formateur.getNom());
                _formateur.setEmail(formateur.getEmail());
                return new ResponseEntity<>(formateurRepository.save(_formateur), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteFormateur(@PathVariable("id") long id) {
        try {
            formateurRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
