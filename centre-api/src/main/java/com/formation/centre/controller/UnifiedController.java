package com.formation.centre.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.centre.dto.CompositionAcquisitionDTO;
import com.formation.centre.dto.ProprieteDTO;
import com.formation.centre.service.UnifiedService;

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
        // payload doit contenir propriete (ProprieteDTO) et compositions (List<CompositionAcquisitionDTO>)
        Object proprieteObj = payload.get("propriete");
        Object compositionsObj = payload.get("compositions");
        return ResponseEntity.ok(unifiedService.createProprieteWithCompositions(utilisateurId, proprieteObj, compositionsObj));
    }

    @PostMapping("/addCompositionsToPropriete/{proprieteId}")
    public ResponseEntity<ProprieteDTO> addCompositionsToPropriete(@PathVariable String proprieteId, @RequestBody List<CompositionAcquisitionDTO> compositions) {
        return ResponseEntity.ok(unifiedService.addCompositionsToPropriete(proprieteId, compositions));
    }
}
