package com.formation.centre.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.formation.centre.model.Formation;
import com.formation.centre.repository.FormationRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/formations")
@CrossOrigin(origins = "http://localhost:5173") // Permet les appels depuis votre frontend
public class FormationController {

    @Autowired
    private FormationRepository formationRepository;

    @GetMapping
    public ResponseEntity<List<Formation>> getAllFormations() {
        try {
            List<Formation> formations = formationRepository.findAll();
            return new ResponseEntity<>(formations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Formation> createFormation(@RequestBody Formation formation) {
        try {
            Formation _formation = formationRepository.save(formation);
            return new ResponseEntity<>(_formation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formation> updateFormation(@PathVariable("id") long id, @RequestBody Formation formation) {
        Optional<Formation> formationData = formationRepository.findById(id);

        if (formationData.isPresent()) {
            Formation _formation = formationData.get();
            _formation.setTitre(formation.getTitre());
            _formation.setDescription(formation.getDescription());
            return new ResponseEntity<>(formationRepository.save(_formation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFormation(@PathVariable("id") long id) {
        try {
            formationRepository.deleteById(id);
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
