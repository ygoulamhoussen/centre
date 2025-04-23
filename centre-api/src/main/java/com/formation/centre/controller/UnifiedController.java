package com.formation.centre.controller;

import com.formation.centre.dto.LocataireDTO;
import com.formation.centre.dto.ProprieteDTO;
import com.formation.centre.dto.LocationDTO;
import com.formation.centre.service.UnifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UnifiedController {

    @Autowired
    private UnifiedService unifiedService;

    @GetMapping("/getProprietesByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<ProprieteDTO>> getProprietesByUtilisateur(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getProprietesByUtilisateur(utilisateurId));
    }

    @PostMapping("/createPropriete/{utilisateurId}")
    public ResponseEntity<ProprieteDTO> createPropriete(@PathVariable String utilisateurId, @RequestBody ProprieteDTO dto) {
        return ResponseEntity.ok(unifiedService.createPropriete(utilisateurId, dto));
    }

    @PostMapping("/createProprieteWithCompositions/{utilisateurId}")
    public ResponseEntity<ProprieteDTO> createProprieteWithCompositions(
            @PathVariable String utilisateurId,
            @RequestBody Map<String, Object> payload) {
        Object proprieteObj = payload.get("propriete");
        Object compositionsObj = payload.get("compositions");
        return ResponseEntity.ok(unifiedService.createProprieteWithCompositions(utilisateurId, proprieteObj, compositionsObj));
    }

    @PostMapping("/addCompositionsToPropriete/{proprieteId}")
    public ResponseEntity<ProprieteDTO> addCompositionsToPropriete(@PathVariable String proprieteId, @RequestBody List<com.formation.centre.dto.CompositionAcquisitionDTO> compositions) {
        return ResponseEntity.ok(unifiedService.addCompositionsToPropriete(proprieteId, compositions));
    }

    @PostMapping("/createLocataire")
    public ResponseEntity<LocataireDTO> createLocataire(@RequestBody LocataireDTO dto) {
        return ResponseEntity.ok(unifiedService.creerLocataire(dto.getUtilisateurId(), dto));
    }

    @GetMapping("/getLocatairesByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<LocataireDTO>> getLocataires(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getLocatairesByUtilisateur(utilisateurId));
    }

    @PutMapping("/updateLocataire")
    public ResponseEntity<LocataireDTO> updateLocataire(@RequestBody LocataireDTO dto) {
        return ResponseEntity.ok(unifiedService.updateLocataire(dto.getId(), dto));
    }

    @DeleteMapping("/deleteLocataire/{locataireId}")
    public ResponseEntity<Void> deleteLocataire(@PathVariable String locataireId) {
        unifiedService.deleteLocataire(locataireId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createLocation")
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO dto) {
        return ResponseEntity.ok(unifiedService.createLocation(dto));
    }

    @PutMapping("/updateLocation")
    public ResponseEntity<LocationDTO> updateLocation(@RequestBody LocationDTO dto) {
        return ResponseEntity.ok(unifiedService.updateLocation(dto.getId(), dto));
    }

    @DeleteMapping("/deleteLocation/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String locationId) {
        unifiedService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getLocationsByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<LocationDTO>> getLocations(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getLocationsByUtilisateur(utilisateurId));
    }
} 
