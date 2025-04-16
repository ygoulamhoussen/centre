package com.formation.centre.controller;

import com.formation.centre.dto.ProprieteDTO;
import com.formation.centre.dto.CompositionAcquisitionDTO;
import com.formation.centre.service.UnifiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/addCompositionsToPropriete/{proprieteId}")
    public ResponseEntity<ProprieteDTO> addCompositionsToPropriete(@PathVariable String proprieteId, @RequestBody List<CompositionAcquisitionDTO> compositions) {
        return ResponseEntity.ok(unifiedService.addCompositionsToPropriete(proprieteId, compositions));
    }
}
