package com.formation.centre.controller;

import com.formation.centre.model.Formateur;
import com.formation.centre.service.FormateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formateurs")
public class FormateurController {

    @Autowired
    private FormateurService formateurService;

    @GetMapping
    public List<Formateur> getAllFormateurs() {
        return formateurService.getAllFormateurs();
    }

    @PostMapping
    public Formateur createFormateur(@RequestBody Formateur formateur) {
        return formateurService.createFormateur(formateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formateur> updateFormateur(@PathVariable Long id, @RequestBody Formateur formateurDetails) {
        Formateur updatedFormateur = formateurService.updateFormateur(id, formateurDetails);
        return ResponseEntity.ok(updatedFormateur);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormateur(@PathVariable Long id) {
        formateurService.deleteFormateur(id);
        return ResponseEntity.noContent().build();
    }
}
