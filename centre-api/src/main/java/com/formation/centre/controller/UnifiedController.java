package com.formation.centre.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.centre.dto.AmortissementDTO;
import com.formation.centre.dto.ChargeDTO;
import com.formation.centre.dto.CompositionAcquisitionDTO;
import com.formation.centre.dto.CreditDTO;
import com.formation.centre.dto.DashboardDTO;
import com.formation.centre.dto.DocumentDTO;
import com.formation.centre.dto.EcheanceCreditDTO;
import com.formation.centre.dto.EcritureComptableDTO;
import com.formation.centre.dto.ImmobilisationDTO;
import com.formation.centre.dto.LocataireDTO;
import com.formation.centre.dto.LocataireDetailDTO;
import com.formation.centre.dto.LocationDTO;
import com.formation.centre.dto.LocationDetailDTO;
import com.formation.centre.dto.PaiementDTO;
import com.formation.centre.dto.ProprieteDTO;
import com.formation.centre.dto.ProprieteDetailDTO;
import com.formation.centre.dto.QuittanceDTO;
import com.formation.centre.dto.RecetteDTO;
import com.formation.centre.dto.ResultatFiscalDTO;
import com.formation.centre.model.Amortissement;
import com.formation.centre.model.Charge;
import com.formation.centre.model.CompositionAcquisition;
import com.formation.centre.model.Propriete;
import com.formation.centre.model.CompteComptable;
import com.formation.centre.repository.ProprieteRepository;
import com.formation.centre.repository.CreditRepository;
import com.formation.centre.service.UnifiedService;


@RestController
@RequestMapping("/api")
public class UnifiedController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    
    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UnifiedService unifiedService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Backend is running!");
    }

    @GetMapping("/getProprietesByUtilisateur/{utilisateurId}")
    public ResponseEntity<List<ProprieteDTO>> getProprietesByUtilisateur(@PathVariable String utilisateurId) {
        return ResponseEntity.ok(unifiedService.getProprietesByUtilisateur(utilisateurId));
    }

    @PostMapping(value = "/createPropriete/{utilisateurId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPropriete(
            @PathVariable String utilisateurId, 
            @RequestBody ProprieteDTO dto) {
        try {
            System.out.println("Reçu une requête pour créer une propriété:");
            System.out.println("Utilisateur ID: " + utilisateurId);
            System.out.println("Données reçues: " + dto);
            
            if (dto.getCompositions() != null) {
                System.out.println("Compositions reçues: " + dto.getCompositions().size());
                dto.getCompositions().forEach(c -> 
                    System.out.println(" - " + c.getCategorie() + ": " + c.getMontant())
                );
            }
            
            ProprieteDTO saved = unifiedService.savePropriete(utilisateurId, dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la création de la propriété : " + e.getMessage());
        }
    }

    @PutMapping("/updatePropriete/{utilisateurId}")
    public ResponseEntity<?> updatePropriete(
            @PathVariable String utilisateurId,
            @RequestBody ProprieteDTO dto) {
        try {
            return ResponseEntity.ok(unifiedService.savePropriete(utilisateurId, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la mise à jour de la propriété : " + e.getMessage());
        }
    }

    @DeleteMapping("/deletePropriete/{proprieteId}")
    public ResponseEntity<?> deletePropriete(@PathVariable String proprieteId) {
        return unifiedService.deletePropriete(proprieteId);
    }
    
    @PostMapping("/createComposition")
    public ResponseEntity<?> createComposition(
            @RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Reçu une requête pour créer une composition:");
            System.out.println("Données reçues: " + payload);
            
            // Récupérer l'ID de la propriété
            String proprieteId = (String) payload.get("proprieteId");
            if (proprieteId == null) {
                return ResponseEntity.badRequest().body("proprieteId est requis");
            }
            
            // Récupérer la propriété existante
            Propriete propriete = proprieteRepository.findById(UUID.fromString(proprieteId))
                .orElseThrow(() -> new IllegalArgumentException("Propriété non trouvée"));
                
            // Créer une nouvelle composition
            CompositionAcquisition composition = new CompositionAcquisition();
            composition.setCategorie((String) payload.get("categorie"));
            composition.setDescription((String) payload.get("description"));
            if (payload.get("montant") != null) {
                composition.setMontant(new BigDecimal(payload.get("montant").toString()));
            }
            composition.setPropriete(propriete);
            
            // Ajouter à la liste des compositions de la propriété
            if (propriete.getCompositions() == null) {
                propriete.setCompositions(new ArrayList<>());
            }
            propriete.getCompositions().add(composition);
            
            // Sauvegarder la propriété avec la nouvelle composition
            Propriete savedPropriete = proprieteRepository.save(propriete);
            
            // Récupérer la composition fraîchement créée
            CompositionAcquisition savedComposition = savedPropriete.getCompositions().stream()
                .filter(c -> c.getCategorie().equals(composition.getCategorie()))
                .filter(c -> (c.getMontant() == null && composition.getMontant() == null) || 
                             (c.getMontant() != null && c.getMontant().compareTo(composition.getMontant()) == 0))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Erreur lors de la création de la composition"));
            
            return ResponseEntity.ok(CompositionAcquisitionDTO.fromEntity(savedComposition));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la création de la composition: " + e.getMessage());
        }
    }

    @PostMapping(value = "/createProprieteWithCompositions/{utilisateurId}", produces = MediaType.APPLICATION_JSON_VALUE)
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
        return ResponseEntity.ok(unifiedService.saveLocataire(dto.getUtilisateurId(), dto));
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


    @PostMapping("/createPaiement")
    public ResponseEntity<PaiementDTO> createPaiement(@RequestBody PaiementDTO dto) {
        return ResponseEntity.ok(unifiedService.savePaiement(dto));
    }

    @PutMapping("/updatePaiement")
    public ResponseEntity<PaiementDTO> updatePaiement(@RequestBody PaiementDTO dto) {
        return ResponseEntity.ok(unifiedService.savePaiement(dto));
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

    @DeleteMapping("/credits/{creditId}")
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
public ResponseEntity<DocumentDTO> uploadDocument(@RequestBody DocumentDTO dto) {
    try {
        DocumentDTO savedDocument = unifiedService.saveDocument(dto);
        return ResponseEntity.ok(savedDocument);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@PostMapping("/uploadDocumentFile")
public ResponseEntity<DocumentDTO> uploadDocumentFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("titre") String titre,
        @RequestParam("utilisateurId") String utilisateurId,
        @RequestParam(value = "proprieteId", required = false) String proprieteId,
        @RequestParam(value = "typeDocument", required = false) String typeDocument) {
    try {
        // Validation de la taille du fichier (10MB = 10 * 1024 * 1024 bytes)
        long maxFileSize = 10 * 1024 * 1024; // 10MB en bytes
        if (file.getSize() > maxFileSize) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Fichier trop volumineux");
            errorResponse.put("message", "La taille du fichier (" + formatFileSize(file.getSize()) + 
                           ") dépasse la limite autorisée (10MB maximum)");
            errorResponse.put("fileSize", file.getSize());
            errorResponse.put("maxSize", maxFileSize);
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
        }

        // Validation du type de fichier
        if (file.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Fichier vide");
            errorResponse.put("message", "Le fichier uploadé est vide");
            return ResponseEntity.badRequest().body(null);
        }

        DocumentDTO dto = new DocumentDTO();
        dto.setTitre(titre);
        dto.setUtilisateurId(utilisateurId);
        dto.setProprieteId(proprieteId);
        dto.setTypeDocument(typeDocument);
        dto.setNomFichier(file.getOriginalFilename());
        dto.setMimeType(file.getContentType());
        dto.setTaille(file.getSize());
        
        // Convertir le fichier en base64 pour le stockage
        String contenu = Base64.getEncoder().encodeToString(file.getBytes());
        dto.setContenu(contenu);
        
        DocumentDTO savedDocument = unifiedService.saveDocument(dto);
        return ResponseEntity.ok(savedDocument);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

// Méthode utilitaire pour formater la taille du fichier
private String formatFileSize(long bytes) {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
    if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
}

@GetMapping("/documents/{id}")
public ResponseEntity<DocumentDTO> getDocument(@PathVariable String id) {
    try {
        DocumentDTO dto = unifiedService.getDocumentById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/documents/{id}/content")
@Transactional(readOnly = true)
public ResponseEntity<String> downloadDocumentContent(@PathVariable String id) {
    try {
        DocumentDTO dto = unifiedService.getDocumentById(id);
        if (dto == null || dto.getContenu() == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Préparer les en-têtes de réponse
        HttpHeaders headers = new HttpHeaders();
        String mimeType = dto.getMimeType() != null ? dto.getMimeType() : "application/octet-stream";
        headers.setContentType(MediaType.TEXT_PLAIN);
        
        // Définir le nom du fichier pour le téléchargement
        String filename = dto.getNomFichier() != null ? dto.getNomFichier() : "document";
        headers.setContentDisposition(ContentDisposition.attachment()
            .filename(filename)
            .build());
            
        return new ResponseEntity<>(dto.getContenu(), headers, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/downloadDocument/{id}")
@Transactional(readOnly = true)
public ResponseEntity<byte[]> downloadDocument(@PathVariable String id) {
    try {
        // Récupérer les métadonnées du document (sans le contenu)
        DocumentDTO dto = unifiedService.getDocumentMetadataById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Récupérer uniquement le contenu binaire du document
        byte[] fileContent = unifiedService.getDocumentContentById(id);
        if (fileContent == null || fileContent.length == 0) {
            return ResponseEntity.notFound().build();
        }

        // Définir les en-têtes de la réponse
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        // Définir le nom du fichier pour le téléchargement
        String fileName = dto.getNomFichier() != null ? dto.getNomFichier() : "document.bin";
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        
        // Retourner le contenu binaire avec les en-têtes appropriés
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@DeleteMapping("/documents/{id}")
public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
    try {
        unifiedService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    } catch (RuntimeException e) {
        if (e.getMessage().contains("introuvable")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/getDocumentsByUtilisateur/{utilisateurId}")
public ResponseEntity<List<DocumentDTO>> getDocumentsByUtilisateur(@PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getDocumentsByUtilisateur(utilisateurId));
}

@GetMapping("/documents/utilisateur/{utilisateurId}")
public ResponseEntity<List<DocumentDTO>> getDocumentsByUtilisateurAlt(@PathVariable String utilisateurId) {
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

// --- PAIEMENTS ---

@GetMapping("/getPaiementsByUtilisateur/{utilisateurId}")
public ResponseEntity<List<PaiementDTO>> getPaiementsByUtilisateur(@PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getPaiementsByUtilisateur(utilisateurId));
}

@GetMapping("/getPaiement/{id}")
public ResponseEntity<PaiementDTO> getPaiement(@PathVariable String id) {
    return ResponseEntity.ok(unifiedService.getPaiementsByUtilisateur(id).stream()
        .filter(p -> p.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'ID: " + id)));
}

@PostMapping("/savePaiement")
public ResponseEntity<PaiementDTO> savePaiement(@RequestBody PaiementDTO paiementDTO) {
    return ResponseEntity.ok(unifiedService.savePaiement(paiementDTO));
}

@DeleteMapping("/deletePaiement/{id}")
public ResponseEntity<Void> deletePaiement(@PathVariable String id) {
    unifiedService.deletePaiement(id);
    return ResponseEntity.ok().build();
}

@GetMapping("/getQuittanceById/{id}")
public ResponseEntity<QuittanceDTO> getQuittanceById(@PathVariable String id) {
    QuittanceDTO dto = unifiedService.getQuittanceById(id);
    if (dto == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quittance non trouvée");
    }
    return ResponseEntity.ok(dto);
}

@GetMapping("/getPaiementsByQuittance/{quittanceId}")
public ResponseEntity<List<PaiementDTO>> getPaiementsByQuittance(@PathVariable String quittanceId) {
    return ResponseEntity.ok(unifiedService.getPaiementsByQuittance(quittanceId));
}

@GetMapping("/getQuittancesByLocation/{locationId}")
public ResponseEntity<List<QuittanceDTO>> getQuittancesByLocation(@PathVariable String locationId) {
    return ResponseEntity.ok(unifiedService.getQuittancesByLocation(locationId));
}

// ===== ENDPOINTS POUR LES IMMOBILISATIONS =====

@GetMapping("/immobilisations/{utilisateurId}")
public ResponseEntity<List<ImmobilisationDTO>> getImmobilisationsByUtilisateur(@PathVariable String utilisateurId) {
    try {
        List<ImmobilisationDTO> immobilisations = unifiedService.getImmobilisationsByUtilisateur(utilisateurId);
        return ResponseEntity.ok(immobilisations);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/immobilisations/propriete/{proprieteId}")
public ResponseEntity<List<ImmobilisationDTO>> getImmobilisationsByPropriete(@PathVariable String proprieteId) {
    try {
        List<ImmobilisationDTO> immobilisations = unifiedService.getImmobilisationsByPropriete(proprieteId);
        return ResponseEntity.ok(immobilisations);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@PostMapping("/immobilisations")
public ResponseEntity<ImmobilisationDTO> createImmobilisation(@RequestBody ImmobilisationDTO dto) {
    try {
        System.out.println("=== CRÉATION IMMOBILISATION ===");
        System.out.println("Données reçues: " + dto);
        
        ImmobilisationDTO saved = unifiedService.saveImmobilisation(dto);
        System.out.println("Immobilisation sauvegardée: " + saved);
        
        return ResponseEntity.ok(saved);
    } catch (Exception e) {
        System.err.println("Erreur lors de la création de l'immobilisation:");
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(null);
    }
}

@PutMapping("/immobilisations")
public ResponseEntity<ImmobilisationDTO> updateImmobilisation(@RequestBody ImmobilisationDTO dto) {
    try {
        ImmobilisationDTO saved = unifiedService.saveImmobilisation(dto);
        return ResponseEntity.ok(saved);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@DeleteMapping("/immobilisations/{id}")
public ResponseEntity<Void> deleteImmobilisation(@PathVariable String id) {
    try {
        unifiedService.deleteImmobilisation(id);
        return ResponseEntity.noContent().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

// ===== ENDPOINTS POUR LES AMORTISSEMENTS =====

@GetMapping("/amortissements/{utilisateurId}")
public ResponseEntity<List<AmortissementDTO>> getAmortissementsByUtilisateur(@PathVariable String utilisateurId) {
    try {
        List<AmortissementDTO> amortissements = unifiedService.getAmortissementsByUtilisateur(utilisateurId);
        return ResponseEntity.ok(amortissements);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/amortissements/propriete/{proprieteId}")
public ResponseEntity<List<AmortissementDTO>> getAmortissementsByPropriete(@PathVariable String proprieteId) {
    try {
        List<AmortissementDTO> amortissements = unifiedService.getAmortissementsByPropriete(proprieteId);
        return ResponseEntity.ok(amortissements);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/amortissements/immobilisation/{immobilisationId}")
public ResponseEntity<List<AmortissementDTO>> getAmortissementsByImmobilisation(@PathVariable String immobilisationId) {
    try {
        List<AmortissementDTO> amortissements = unifiedService.getAmortissementsByImmobilisation(immobilisationId);
        return ResponseEntity.ok(amortissements);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/amortissements/{utilisateurId}/{annee}")
public ResponseEntity<List<AmortissementDTO>> getAmortissementsByUtilisateurAndAnnee(
        @PathVariable String utilisateurId,
        @PathVariable int annee) {
    try {
        List<AmortissementDTO> amortissements = unifiedService.getAmortissementsByUtilisateurAndAnnee(utilisateurId, annee);
        return ResponseEntity.ok(amortissements);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@PostMapping("/amortissements/generer/{immobilisationId}")
public ResponseEntity<Void> genererPlanAmortissement(@PathVariable String immobilisationId) {
    try {
        unifiedService.genererPlanAmortissement(immobilisationId);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
public ResponseEntity<?> handleOptions() {
    return ResponseEntity.ok().build();
}

// ===== ENDPOINTS POUR LES CHARGES =====

@GetMapping("/getChargesByUtilisateur/{utilisateurId}")
public ResponseEntity<List<ChargeDTO>> getChargesByUtilisateur(@PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getChargesByUtilisateur(utilisateurId));
}

@GetMapping("/charges/{utilisateurId}")
public ResponseEntity<List<ChargeDTO>> getChargesByUtilisateurAlt(@PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getChargesByUtilisateur(utilisateurId));
}

@GetMapping("/charges/propriete/{proprieteId}")
public ResponseEntity<List<ChargeDTO>> getChargesByPropriete(@PathVariable String proprieteId) {
    return ResponseEntity.ok(unifiedService.getChargesByPropriete(proprieteId));
}

@PostMapping("/charges")
public ResponseEntity<ChargeDTO> createCharge(@RequestBody ChargeDTO dto) {
    return ResponseEntity.ok(unifiedService.saveCharge(dto));
}

@PutMapping("/charges")
public ResponseEntity<ChargeDTO> updateCharge(@RequestBody ChargeDTO dto) {
    return ResponseEntity.ok(unifiedService.saveCharge(dto));
}

@DeleteMapping("/charges/{id}")
public ResponseEntity<Void> deleteCharge(@PathVariable String id) {
    unifiedService.deleteCharge(id);
    return ResponseEntity.noContent().build();
}

@GetMapping("/charge/{id}")
public ResponseEntity<ChargeDTO> getChargeById(@PathVariable String id) {
    ChargeDTO dto = unifiedService.getChargeById(id);
    if (dto == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(dto);
}

// ===== ENDPOINTS POUR LES RECETTES =====

@GetMapping("/getRecettesByUtilisateur/{utilisateurId}")
public ResponseEntity<List<RecetteDTO>> getRecettesByUtilisateur(@PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getRecettesByUtilisateur(utilisateurId));
}

@GetMapping("/recettes/{utilisateurId}")
public ResponseEntity<List<RecetteDTO>> getRecettesByUtilisateurAlt(@PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getRecettesByUtilisateur(utilisateurId));
}

@GetMapping("/recettes/propriete/{proprieteId}")
public ResponseEntity<List<RecetteDTO>> getRecettesByPropriete(@PathVariable String proprieteId) {
    return ResponseEntity.ok(unifiedService.getRecettesByPropriete(proprieteId));
}

@PostMapping("/recettes")
public ResponseEntity<RecetteDTO> createRecette(@RequestBody RecetteDTO dto) {
    if (dto.getType() == null || dto.getType().trim().isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le champ 'type' de la recette est obligatoire.");
    }
    return ResponseEntity.ok(unifiedService.saveRecette(dto));
}

@PutMapping("/recettes")
public ResponseEntity<RecetteDTO> updateRecette(@RequestBody RecetteDTO dto) {
    if (dto.getType() == null || dto.getType().trim().isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le champ 'type' de la recette est obligatoire.");
    }
    return ResponseEntity.ok(unifiedService.saveRecette(dto));
}

@DeleteMapping("/recettes/{id}")
public ResponseEntity<Void> deleteRecette(@PathVariable String id) {
    unifiedService.deleteRecette(id);
    return ResponseEntity.noContent().build();
}

@GetMapping("/recette/{id}")
public ResponseEntity<RecetteDTO> getRecetteById(@PathVariable String id) {
    RecetteDTO dto = unifiedService.getRecetteById(id);
    if (dto == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(dto);
}

// ===== ENDPOINTS POUR LES ÉCRITURES COMPTABLES =====

@GetMapping("/ecritures-comptables/{proprieteId}/{anneeFiscale}")
public ResponseEntity<List<EcritureComptableDTO>> getEcrituresComptables(
        @PathVariable String proprieteId, 
        @PathVariable int anneeFiscale) {
    return ResponseEntity.ok(unifiedService.getEcrituresComptables(proprieteId, anneeFiscale));
}

@GetMapping("/ecritures-comptables/utilisateur/{utilisateurId}")
public ResponseEntity<List<EcritureComptableDTO>> getEcrituresComptablesByUtilisateur(
        @PathVariable String utilisateurId) {
    return ResponseEntity.ok(unifiedService.getEcrituresComptablesByUtilisateur(utilisateurId));
}

@GetMapping("/ecritures-comptables/utilisateur/{utilisateurId}/{anneeFiscale}")
public ResponseEntity<List<EcritureComptableDTO>> getEcrituresComptablesByUtilisateurAndAnnee(
        @PathVariable String utilisateurId,
        @PathVariable int anneeFiscale) {
    return ResponseEntity.ok(unifiedService.getEcrituresComptablesByUtilisateurAndAnnee(utilisateurId, anneeFiscale));
}

@PostMapping("/ecritures-comptables/charge/{chargeId}")
public ResponseEntity<EcritureComptableDTO> createEcritureComptableCharge(@PathVariable String chargeId) {
    return ResponseEntity.ok(unifiedService.createEcritureComptableCharge(chargeId));
}

@PostMapping("/ecritures-comptables/recette/{recetteId}")
public ResponseEntity<EcritureComptableDTO> createEcritureComptableRecette(@PathVariable String recetteId) {
    return ResponseEntity.ok(unifiedService.createEcritureComptableRecette(recetteId));
}

@PostMapping("/ecritures-comptables/quittance/{quittanceId}")
public ResponseEntity<?> createEcritureComptableQuittance(@PathVariable String quittanceId) {
    try {
        EcritureComptableDTO result = unifiedService.createEcritureComptableQuittance(quittanceId);
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Erreur de création");
        errorResponse.put("message", e.getMessage());
        errorResponse.put("quittanceId", quittanceId);
        return ResponseEntity.badRequest().body(errorResponse);
    } catch (Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Erreur interne");
        errorResponse.put("message", e.getMessage());
        errorResponse.put("quittanceId", quittanceId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

@PutMapping("/ecritures-comptables")
public ResponseEntity<EcritureComptableDTO> updateEcritureComptable(@RequestBody EcritureComptableDTO dto) {
    return ResponseEntity.ok(unifiedService.updateEcritureComptable(dto));
}

@DeleteMapping("/ecritures-comptables/{id}")
public ResponseEntity<Void> deleteEcritureComptable(@PathVariable String id) {
    unifiedService.deleteEcritureComptable(id);
    return ResponseEntity.noContent().build();
}

@PostMapping("/createEcheanceCredit")
public ResponseEntity<EcheanceCreditDTO> createEcheanceCredit(@RequestBody EcheanceCreditDTO dto) {
    return ResponseEntity.ok(unifiedService.saveEcheanceCredit(dto));
}

@GetMapping("/getEcheancesByCredit/{creditId}")
public ResponseEntity<List<EcheanceCreditDTO>> getEcheancesByCredit(@PathVariable String creditId) {
    List<EcheanceCreditDTO> result = unifiedService.getEcheancesByCredit(creditId);
    if (result == null) result = new ArrayList<>();
    return ResponseEntity.ok(result);
}

@PostMapping("/createEcheancesCredit")
public ResponseEntity<?> createEcheancesCredit(@RequestBody List<EcheanceCreditDTO> echeances) {
    List<EcheanceCreditDTO> saved = new java.util.ArrayList<>();
    for (EcheanceCreditDTO echeance : echeances) {
        EcheanceCreditDTO savedEcheance = unifiedService.saveEcheanceCredit(echeance);
        saved.add(savedEcheance);
        // Récupérer le crédit pour proprieteId et utilisateurId
        var creditOpt = creditRepository.findById(java.util.UUID.fromString(echeance.getCreditId()));
        if (creditOpt.isPresent()) {
            var credit = creditOpt.get();
            String proprieteId = credit.getPropriete().getId().toString();
            String utilisateurId = credit.getPropriete().getUtilisateur().getId().toString();
            // Charge d'intérêt
            if (echeance.getInteret() != null && !echeance.getInteret().equals("0")) {
                ChargeDTO chargeInteret = new ChargeDTO();
                chargeInteret.setProprieteId(proprieteId);
                chargeInteret.setUtilisateurId(utilisateurId);
                chargeInteret.setNature("INTERETS_EMPRUNT");
                chargeInteret.setDateCharge(echeance.getDateEcheance());
                chargeInteret.setMontant(echeance.getInteret());
                chargeInteret.setIntitule("Intérêts échéance crédit");
                chargeInteret.setStatut("PAYEE");
                unifiedService.saveCharge(chargeInteret);
            }
            // Charge d'assurance
            if (echeance.getAssurance() != null && !echeance.getAssurance().equals("0")) {
                ChargeDTO chargeAssurance = new ChargeDTO();
                chargeAssurance.setProprieteId(proprieteId);
                chargeAssurance.setUtilisateurId(utilisateurId);
                chargeAssurance.setNature("ASSURANCE_EMPRUNT");
                chargeAssurance.setDateCharge(echeance.getDateEcheance());
                chargeAssurance.setMontant(echeance.getAssurance());
                chargeAssurance.setIntitule("Assurance échéance crédit");
                chargeAssurance.setStatut("PAYEE");
                unifiedService.saveCharge(chargeAssurance);
            }
        }
    }
    return ResponseEntity.ok(saved);
}

@GetMapping("/resultat-fiscal")
public ResponseEntity<ResultatFiscalDTO> getResultatFiscal(
        @RequestParam("annee") int annee,
        @RequestParam("proprieteIds") List<String> proprieteIds,
        @RequestParam("utilisateurId") String utilisateurId) {
    ResultatFiscalDTO resultat = unifiedService.calculerResultatFiscal(annee, proprieteIds, utilisateurId);
    return ResponseEntity.ok(resultat);
}

@GetMapping("/journal-comptable/pdf")
public ResponseEntity<byte[]> genererJournalComptablePdf(
        @RequestParam(value = "proprieteId", required = false) String proprieteId,
        @RequestParam(value = "utilisateurId", required = false) String utilisateurId,
        @RequestParam("annee") int annee) {
    try {
        byte[] pdf = unifiedService.genererJournalComptablePdf(proprieteId, utilisateurId, annee);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=journal-comptable-" + annee + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}

@GetMapping("/plan-comptable")
public List<CompteComptable> getPlanComptable() {
    return unifiedService.getPlanComptable();
}

} 
