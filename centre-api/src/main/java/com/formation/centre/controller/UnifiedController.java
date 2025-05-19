package com.formation.centre.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.centre.dto.CreditDTO;
import com.formation.centre.dto.DashboardDTO;
import com.formation.centre.dto.DocumentDTO;
import com.formation.centre.dto.LocataireDTO;
import com.formation.centre.dto.LocataireDetailDTO;
import com.formation.centre.dto.LocationDTO;
import com.formation.centre.dto.LocationDetailDTO;
import com.formation.centre.dto.PaiementDTO;
import com.formation.centre.dto.ProprieteDTO;
import com.formation.centre.dto.ProprieteDetailDTO;
import com.formation.centre.dto.QuittanceDTO;
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
        return ResponseEntity.ok(unifiedService.savePropriete(utilisateurId, dto));
    }

    @PutMapping("/updatePropriete")
    public ResponseEntity<ProprieteDTO> updatePropriete(@RequestBody ProprieteDTO dto) {
        return ResponseEntity.ok(unifiedService.savePropriete(dto.getId(), dto));
    }

    @DeleteMapping("/deletePropriete/{proprieteId}")
    public ResponseEntity<Void> deletePropriete(@PathVariable String proprieteId) {
        unifiedService.deletePropriete(proprieteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createProprieteWithCompositions/{utilisateurId}")
    public ResponseEntity<ProprieteDTO> createProprieteWithCompositions(
            @PathVariable String utilisateurId,
            @RequestBody Map<String, Object> payload) {
                Object compositionsObj = payload.get("compositions");
                Object proprieteObj = payload.get("propriete");

                ObjectMapper mapper = new ObjectMapper();
                ProprieteDTO proprieteDTO = mapper.convertValue(proprieteObj, ProprieteDTO.class);

        return ResponseEntity.ok(unifiedService.savePropriete(utilisateurId, proprieteDTO));
    }

/*     @PostMapping("/addCompositionsToPropriete/{proprieteId}")
    public ResponseEntity<ProprieteDTO> addCompositionsToPropriete(@PathVariable String proprieteId, @RequestBody List<CompositionAcquisitionDTO> compositions) {
        return ResponseEntity.ok(unifiedService.addCompositionsToPropriete(proprieteId, compositions));
    } */

    @PostMapping("/createLocataire")
    public ResponseEntity<LocataireDTO> createLocataire(@RequestBody LocataireDTO dto) {
        return ResponseEntity.ok(unifiedService.saveLocataire(dto.getUtilisateurId(), dto));
    }

    @GetMapping("/getLocatairesByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<LocataireDTO>> getLocataires(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getLocatairesByUtilisateur(utilisateurId));
    }

    @PutMapping("/updateLocataire")
    public ResponseEntity<LocataireDTO> updateLocataire(@RequestBody LocataireDTO dto) {
        return ResponseEntity.ok(unifiedService.saveLocataire(dto.getId(), dto));
    }

    @DeleteMapping("/deleteLocataire/{locataireId}")
    public ResponseEntity<Void> deleteLocataire(@PathVariable String locataireId) {
        unifiedService.deleteLocataire(locataireId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createLocation")
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO dto) {
        return ResponseEntity.ok(unifiedService.saveLocation(dto));
    }

    @PutMapping("/updateLocation")
    public ResponseEntity<LocationDTO> updateLocation(@RequestBody LocationDTO dto) {
        return ResponseEntity.ok(unifiedService.saveLocation(dto));
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

    @GetMapping("/getQuittancesByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<QuittanceDTO>> getQuittances(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getQuittancesByUtilisateur(utilisateurId));
    }

    @PostMapping("/createQuittance")
    public ResponseEntity<QuittanceDTO> createQuittance(@RequestBody QuittanceDTO dto) {
        return ResponseEntity.ok(unifiedService.saveQuittance(dto));
    }

    @PutMapping("/updateQuittance")
    public ResponseEntity<QuittanceDTO> updateQuittance(@RequestBody QuittanceDTO dto) {
        return ResponseEntity.ok(unifiedService.saveQuittance(dto));
    }

    @DeleteMapping("/deleteQuittance/{quittanceId}")
    public ResponseEntity<Void> deleteQuittance(@PathVariable String quittanceId) {
        unifiedService.deleteQuittance(quittanceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getPaiementsByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<PaiementDTO>> getPaiements(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getPaiementsByUtilisateur(utilisateurId));
    }

    @PostMapping("/createPaiement")
    public ResponseEntity<PaiementDTO> createPaiement(@RequestBody PaiementDTO dto) {
        return ResponseEntity.ok(unifiedService.savePaiement(dto));
    }

    @PutMapping("/updatePaiement")
    public ResponseEntity<PaiementDTO> updatePaiement(@RequestBody PaiementDTO dto) {
        return ResponseEntity.ok(unifiedService.savePaiement(dto));
    }

    @DeleteMapping("/deletePaiement/{paiementId}")
    public ResponseEntity<Void> deletePaiement(@PathVariable String paiementId) {
        unifiedService.deletePaiement(paiementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getCreditsByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<CreditDTO>> getCredits(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getCreditsByUtilisateur(utilisateurId));
    }

    @PostMapping("/createCredit")
    public ResponseEntity<CreditDTO> createCredit(@RequestBody CreditDTO dto) {
        return ResponseEntity.ok(unifiedService.saveCredit(dto));
    }

    @PutMapping("/updateCredit")
    public ResponseEntity<CreditDTO> updateCredit(@RequestBody CreditDTO dto) {
        return ResponseEntity.ok(unifiedService.saveCredit(dto));
    }

    @DeleteMapping("/deleteCredit/{creditId}")
    public ResponseEntity<Void> deleteCredit(@PathVariable String creditId) {
        unifiedService.deleteCredit(creditId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/generateQuittancePdf/{quittanceId}")
public ResponseEntity<byte[]> generateQuittancePdf(@PathVariable String quittanceId) {
    byte[] pdfBytes = unifiedService.generateQuittancePdf(quittanceId);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("quittance.pdf").build());

    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
}

@PostMapping("/uploadDocument")
public DocumentDTO uploadDocument(@RequestBody DocumentDTO dto) {
//byte[] data = Base64.getDecoder().decode(dto.getContenu());
    return unifiedService.saveDocument(dto.getContenu(), dto.getUtilisateurId(), dto.getProprieteId(), dto.getLocataireId(), dto.getTypeDocument(), dto.getTitre(), dto.getDateDocument());
}

@GetMapping("/downloadDocument/{id}")
public ResponseEntity<String> downloadDocument(@PathVariable String id) {
    DocumentDTO dto = unifiedService.getDocumentById(id);
    if (dto.getContenu() == null) {
        return ResponseEntity.notFound().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDisposition(ContentDisposition.attachment().filename(dto.getTitre() != null ? dto.getTitre() : "document.bin").build());

    return new ResponseEntity<>(dto.getContenu(), headers, HttpStatus.OK);
}

@GetMapping("/getDocumentsByUtilisateur/{utilisateurId}")
public ResponseEntity<List<DocumentDTO>> getDocumentsByUtilisateur(@PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getDocumentsByUtilisateur(utilisateurId));
}

@GetMapping("/dashboard/{utilisateurId}")
public DashboardDTO getDashboard(@PathVariable String utilisateurId) {
    return unifiedService.getDashboard(utilisateurId);
}

@GetMapping("/getLocationDetail/{id}")
public ResponseEntity<LocationDetailDTO> getLocationDetail(@PathVariable String id) {
    return ResponseEntity.ok(unifiedService.getLocationDetail(id));
}

@GetMapping("/getLocataireDetails/{locataireId}")
public LocataireDetailDTO getLocataireDetails(@PathVariable UUID locataireId) {
    return unifiedService.getLocataireDetails(locataireId);
}



@GetMapping("/getProprieteDetails/{proprieteId}")
public ProprieteDetailDTO getProprieteDetails(@PathVariable UUID proprieteId) { 

        return unifiedService.getProprieteDetail(proprieteId);

}


} 
