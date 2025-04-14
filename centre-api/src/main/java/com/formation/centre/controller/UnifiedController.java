/* package com.formation.centre.controller;

import com.formation.centre.model.Formateur;
import com.formation.centre.model.Utilisateur;
import com.formation.centre.model.Formation;
import com.formation.centre.repository.FormateurRepository;
import com.formation.centre.repository.UtilisateurRepository;
import com.formation.centre.repository.FormationRepository;
import com.formation.centre.service.FormationService;
import com.formation.centre.service.FormateurService;
import com.formation.centre.dto.FormateurDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class UnifiedController {

    // Formateur endpoints
    @Autowired
    private FormateurService formateurService;

    @GetMapping("/api/formateurs")
    public ResponseEntity<List<FormateurDTO>> getAllFormateurs() {
        try {
            List<FormateurDTO> dtos = formateurService.getAllFormateursDTO();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/api/formateurs/creer")
    public ResponseEntity<FormateurDTO> createFormateur(@RequestBody FormateurDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty() ||
            dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            FormateurDTO savedDto = formateurService.createFormateurDTO(dto);
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/api/formateurs/update/{id}")
    public ResponseEntity<FormateurDTO> updateFormateur(@PathVariable("id") long id, @RequestBody FormateurDTO dto) {
        try {
            Optional<FormateurDTO> updated = formateurService.updateFormateurDTO(id, dto);
            if(updated.isPresent()){
               return new ResponseEntity<>(updated.get(), HttpStatus.OK);
            } else {
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/api/formateurs/delete/{id}")
    public ResponseEntity<HttpStatus> deleteFormateur(@PathVariable("id") long id) {
        try {
            formateurService.deleteFormateur(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/api/formateurs", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleFormateurOptions() {
        return ResponseEntity.ok().build();
    }

    // Utilisateur endpoints
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/api/utilisateurs/login")
    public ResponseEntity<?> loginUser(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur existingUser = utilisateurRepository.findByEmailAndMotDePasse(utilisateur.getEmail(), utilisateur.getMotDePasse());
            if (existingUser != null) {
                return ResponseEntity.ok(existingUser);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/utilisateurs")
    public ResponseEntity<?> createUser(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur _utilisateur = utilisateurRepository.save(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(_utilisateur);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/api/utilisateurs/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        try {
            utilisateurRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Formation endpoints
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationService formationService;

    @GetMapping("/api/formations")
    public ResponseEntity<List<Formation>> getAllFormations() {
        try {
            List<Formation> formations = formationRepository.findAll();
            return new ResponseEntity<>(formations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/formations")
    public ResponseEntity<Formation> createFormation(@RequestBody Formation formation) {
        try {
            Formation _formation = formationRepository.save(formation);
            return new ResponseEntity<>(_formation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/formations/{id}")
    public ResponseEntity<Formation> updateFormation(@PathVariable("id") long id, @RequestBody Formation formation) {
        try {
            Optional<Formation> formationData = formationRepository.findById(id);
            if (formationData.isPresent()) {
                Formation _formation = formationData.get();
                _formation.setTitre(formation.getTitre());
                _formation.setDescription(formation.getDescription());
                return new ResponseEntity<>(formationRepository.save(_formation), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/formations/{id}")
    public ResponseEntity<HttpStatus> deleteFormation(@PathVariable("id") long id) {
        try {
            formationRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/api/formations", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleFormationOptions() {
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/api/formations/search")
    public ResponseEntity<List<Formation>> searchFormations(@RequestParam(required = false) String searchString) {
        try {
            List<Formation> formations = formationService.searchFormations(searchString);
            return new ResponseEntity<>(formations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} */