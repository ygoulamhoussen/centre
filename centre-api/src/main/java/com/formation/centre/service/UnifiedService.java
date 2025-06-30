package com.formation.centre.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.formation.centre.model.Amortissement;
import com.formation.centre.model.Charge;
import com.formation.centre.model.CompositionAcquisition;
import com.formation.centre.model.Credit;
import com.formation.centre.model.DocumentEntity;
import com.formation.centre.model.EcheanceCredit;
import com.formation.centre.model.EcritureComptable;
import com.formation.centre.model.Immobilisation;
import com.formation.centre.model.Locataire;
import com.formation.centre.model.Location;
import com.formation.centre.model.Paiement;
import com.formation.centre.model.Propriete;
import com.formation.centre.model.Quittance;
import com.formation.centre.model.Recette;
import com.formation.centre.model.Utilisateur;
import com.formation.centre.repository.AmortissementRepository;
import com.formation.centre.repository.ChargeRepository;
import com.formation.centre.repository.CreditRepository;
import com.formation.centre.repository.DocumentEntityRepository;
import com.formation.centre.repository.EcheanceCreditRepository;
import com.formation.centre.repository.EcritureComptableRepository;
import com.formation.centre.repository.ImmobilisationRepository;
import com.formation.centre.repository.LocataireRepository;
import com.formation.centre.repository.LocationRepository;
import com.formation.centre.repository.PaiementRepository;
import com.formation.centre.repository.ProprieteRepository;
import com.formation.centre.repository.QuittanceRepository;
import com.formation.centre.repository.RecetteRepository;
import com.formation.centre.repository.UtilisateurRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.formation.centre.dto.ResultatFiscalDTO;
import lombok.RequiredArgsConstructor;
import com.formation.centre.dto.ResultatFiscalDetailDTOs.*;

@Service
@RequiredArgsConstructor
public class UnifiedService {

    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private ProprieteRepository proprieteRepository;
    @Autowired private LocataireRepository locataireRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private QuittanceRepository quittanceRepository;
    @Autowired private PaiementRepository paiementRepository;
    @Autowired private CreditRepository creditRepository;
    @Autowired private DocumentEntityRepository documentEntityRepository;
    @Autowired private AmortissementRepository amortissementRepository;
    @Autowired private ChargeRepository chargeRepository;
    @Autowired private EcritureComptableRepository ecritureComptableRepository;
    @Autowired private RecetteRepository recetteRepository;
    @Autowired private ImmobilisationRepository immobilisationRepository;
    @Autowired private EcheanceCreditRepository echeanceCreditRepository;



    // --- PROPRIETE ---

    @Transactional
    public ResponseEntity<?> deletePropriete(String proprieteIdStr) {
        try {
            UUID proprieteId = UUID.fromString(proprieteIdStr);
            
            // Vérifier s'il existe des locations liées
            boolean hasLocations = locationRepository.existsByProprieteId(proprieteId);
            if (hasLocations) {
                List<Location> locations = locationRepository.findByProprieteId(proprieteId);
                List<String> locationsInfo = locations.stream()
                    .map(l -> "Location #" + l.getId() + " (du " + l.getDateDebut() + ")")
                    .collect(Collectors.toList());
                
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "message", "Impossible de supprimer cette propriété car elle est liée à des locations existantes.",
                        "locations", locationsInfo
                    ));
            }
            
            // Si pas de locations, on peut supprimer la propriété
            proprieteRepository.deleteById(proprieteId);
            return ResponseEntity.noContent().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "ID de propriété invalide", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "message", "Erreur lors de la suppression de la propriété",
                    "details", e.getMessage()
                ));
        }
    }
    
    public List<ProprieteDTO> getProprietesByUtilisateur(String utilisateurId) {
        UUID userId = UUID.fromString(utilisateurId);
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        return proprieteRepository.findAll().stream()
                .filter(p -> p.getUtilisateur().getId().equals(userId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    

    public ProprieteDTO saveProprietebis(String utilisateurId, ProprieteDTO proprieteDTO) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            Utilisateur utilisateur = utilisateurRepository.findById(UUID.fromString(utilisateurId))
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

            Propriete entity = new Propriete();
            entity.setNom(proprieteDTO.getNom());
            entity.setAdresse(proprieteDTO.getAdresse());
            entity.setComplementAdresse(proprieteDTO.getComplementAdresse());
            entity.setCodePostal(proprieteDTO.getCodePostal());
            entity.setVille(proprieteDTO.getVille());
            entity.setTypeBien(Propriete.TypeBien.valueOf(proprieteDTO.getTypeBien()));
            entity.setDateAcquisition(proprieteDTO.getDateAcquisition() != null ? LocalDate.parse(proprieteDTO.getDateAcquisition()) : null);
            entity.setDateLivraison(proprieteDTO.getDateLivraison() != null ? LocalDate.parse(proprieteDTO.getDateLivraison()) : null);
            entity.setMontantAcquisition(proprieteDTO.getMontantAcquisition() != null ? new BigDecimal(proprieteDTO.getMontantAcquisition()) : null);
            entity.setTantieme(proprieteDTO.getTantieme() != null ? new BigDecimal(proprieteDTO.getTantieme()) : null);
            entity.setFraisNotaire(proprieteDTO.getFraisNotaire() != null ? new BigDecimal(proprieteDTO.getFraisNotaire()) : null);
            entity.setFraisAgence(proprieteDTO.getFraisAgence() != null ? new BigDecimal(proprieteDTO.getFraisAgence()) : null);
            entity.setUtilisateur(utilisateur);

            // Traite les compositions si présentes
            List<CompositionAcquisition> compositions = new ArrayList<>();
            if (proprieteDTO.getCompositions() != null) {
                List<?> compList = (List<?>) proprieteDTO.getCompositions();
                for (Object compObj : compList) {
                    CompositionAcquisitionDTO cdto = mapper.convertValue(compObj, CompositionAcquisitionDTO.class);
                    CompositionAcquisition comp = new CompositionAcquisition();
                    comp.setCategorie(cdto.getCategorie());
                    comp.setMontant(new BigDecimal(cdto.getMontant()));
                    comp.setDescription(cdto.getDescription());
                    comp.setPropriete(entity);
                    compositions.add(comp);
                }
            }
            entity.setCompositions(compositions);

            Propriete saved = proprieteRepository.save(entity);
            return toDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la propriété et des compositions : " + e.getMessage(), e);
        }
    }


    public ProprieteDTO savePropriete(String utilisateurId, ProprieteDTO dto) {
        Utilisateur user = utilisateurRepository.findById(UUID.fromString(utilisateurId))
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        
        Propriete p;
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            p = proprieteRepository.findById(UUID.fromString(dto.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Propriété introuvable"));
            
            // Vérifier que l'utilisateur est bien le propriétaire
            if (!p.getUtilisateur().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Non autorisé à modifier cette propriété");
            }
        } else {
            p = new Propriete();
            p.setUtilisateur(user);
            p.setCompositions(new ArrayList<>());
        }
        
        // Mise à jour des champs de base
        p.setNom(dto.getNom());
        p.setAdresse(dto.getAdresse());
        p.setComplementAdresse(dto.getComplementAdresse());
        p.setCodePostal(dto.getCodePostal());
        p.setVille(dto.getVille());
        p.setTypeBien(Propriete.TypeBien.valueOf(dto.getTypeBien()));
        p.setDateAcquisition(dto.getDateAcquisition() != null ? LocalDate.parse(dto.getDateAcquisition()) : null);
        p.setDateLivraison(dto.getDateLivraison() != null ? LocalDate.parse(dto.getDateLivraison()) : null);
        p.setMontantAcquisition(dto.getMontantAcquisition() != null ? new BigDecimal(dto.getMontantAcquisition()) : null);
        p.setTantieme(dto.getTantieme() != null ? new BigDecimal(dto.getTantieme()) : null);
        p.setFraisNotaire(dto.getFraisNotaire() != null ? new BigDecimal(dto.getFraisNotaire()) : null);
        p.setFraisAgence(dto.getFraisAgence() != null ? new BigDecimal(dto.getFraisAgence()) : null);

        // Gestion des compositions
        if (dto.getCompositions() != null) {
            // Initialiser la liste des compositions si nécessaire
            if (p.getCompositions() == null) {
                p.setCompositions(new ArrayList<>());
            }
            
            // Créer une copie de la liste pour itération
            List<CompositionAcquisition> compositionsExistantes = new ArrayList<>(p.getCompositions());
            
            // Supprimer les compositions qui ne sont plus dans le DTO
            compositionsExistantes.stream()
                .filter(existante -> dto.getCompositions().stream()
                    .noneMatch(cdto -> cdto.getId() != null && cdto.getId().equals(existante.getId().toString())))
                .forEach(compoASupprimer -> p.getCompositions().remove(compoASupprimer));

            // Ajouter ou mettre à jour les compositions
            for (CompositionAcquisitionDTO cdto : dto.getCompositions()) {
                CompositionAcquisition c;
                if (cdto.getId() != null && !cdto.getId().isEmpty()) {
                    c = p.getCompositions().stream()
                        .filter(comp -> comp.getId().toString().equals(cdto.getId()))
                        .findFirst()
                        .orElse(new CompositionAcquisition());
                    
                    if (!p.getCompositions().contains(c)) {
                        c.setPropriete(p);
                        p.getCompositions().add(c);
                    }
                } else {
                    c = new CompositionAcquisition();
                    c.setPropriete(p);
                    p.getCompositions().add(c);
                }
                c.setCategorie(cdto.getCategorie());
                c.setMontant(new BigDecimal(cdto.getMontant()));
                c.setDescription(cdto.getDescription());
            }
        } else {
            // Si aucune composition n'est fournie, on vide la liste
            p.setCompositions(new ArrayList<>());
        }

        Propriete saved = proprieteRepository.save(p);
        return toDTO(saved);
    }

/*     public void deletePropriete(String proprieteId) {
        proprieteRepository.deleteById(UUID.fromString(proprieteId));
    } */
    


    private ProprieteDTO toDTO(Propriete p) {
        if (p == null) {
            return null;
        }
        
        ProprieteDTO dto = new ProprieteDTO();
        dto.setId(p.getId() != null ? p.getId().toString() : null);
        dto.setNom(p.getNom());
        dto.setAdresse(p.getAdresse());
        dto.setComplementAdresse(p.getComplementAdresse());
        dto.setCodePostal(p.getCodePostal());
        dto.setVille(p.getVille());
        dto.setTypeBien(p.getTypeBien() != null ? p.getTypeBien().toString() : null);
        dto.setDateAcquisition(p.getDateAcquisition() != null ? p.getDateAcquisition().toString() : null);
        dto.setDateLivraison(p.getDateLivraison() != null ? p.getDateLivraison().toString() : null);
        dto.setMontantAcquisition(p.getMontantAcquisition() != null ? p.getMontantAcquisition().toPlainString() : null);
        dto.setTantieme(p.getTantieme() != null ? p.getTantieme().toPlainString() : null);
        dto.setFraisNotaire(p.getFraisNotaire() != null ? p.getFraisNotaire().toPlainString() : null);
        dto.setFraisAgence(p.getFraisAgence() != null ? p.getFraisAgence().toPlainString() : null);
        
        // Conversion des compositions
        if (p.getCompositions() != null) {
            List<CompositionAcquisitionDTO> compositionsDTO = p.getCompositions().stream()
                .map(CompositionAcquisitionDTO::fromEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            dto.setCompositions(compositionsDTO);
        }
        return dto;
    }

    // --- LOCATAIRE ---
    public List<LocataireDTO> getLocatairesByUtilisateur(String utilisateurId) {
        UUID uid = UUID.fromString(utilisateurId);
        return locataireRepository.findByUtilisateur_Id(uid)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public LocataireDTO saveLocataire(String utilisateurId, LocataireDTO dto) {
        Utilisateur user = utilisateurRepository.findById(UUID.fromString(utilisateurId))
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        Locataire l;
        if (dto.getId()!=null && !dto.getId().isEmpty()) {
            l = locataireRepository.findById(UUID.fromString(dto.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Locataire introuvable"));
        } else {
            l = new Locataire();
            l.setId(UUID.randomUUID());
            l.setUtilisateur(user);
            l.setCreeLe(LocalDateTime.now());
        }
        l.setNom(dto.getNom());
        l.setTelephone(dto.getTelephone());
        l.setEmail(dto.getEmail());
        l.setAdresse(dto.getAdresse());
        l.setComplementAdresse(dto.getComplementAdresse());
        l.setCodePostal(dto.getCodePostal());
        l.setVille(dto.getVille());
        l.setModifieLe(LocalDateTime.now());

        Locataire saved = locataireRepository.save(l);
        return toDto(saved);
    }

    public void deleteLocataire(String locataireId) {
        locataireRepository.deleteById(UUID.fromString(locataireId));
    }

    private LocataireDTO toDto(Locataire l) {
        LocataireDTO dto = new LocataireDTO();
        dto.setId(l.getId().toString());
        dto.setNom(l.getNom());
        dto.setTelephone(l.getTelephone());
        dto.setEmail(l.getEmail());
        dto.setAdresse(l.getAdresse());
        dto.setComplementAdresse(l.getComplementAdresse());
        dto.setCodePostal(l.getCodePostal());
        dto.setVille(l.getVille());
        return dto;
    }

    // --- LOCATION ---
    public List<LocationDTO> getLocationsByUtilisateur(String utilisateurId) {
        UUID uid = UUID.fromString(utilisateurId);
        return locationRepository.findByPropriete_Utilisateur_Id(uid)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public LocationDTO saveLocation(LocationDTO dto) {
        Location loc;
        if (dto.getId()!=null && !dto.getId().isEmpty()) {
            loc = locationRepository.findById(UUID.fromString(dto.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Location introuvable"));
        } else {
            loc = new Location();
            loc.setId(UUID.randomUUID());
            loc.setCreeLe(LocalDateTime.now());
        }
        loc.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId())).orElseThrow());
        loc.setLocataire(locataireRepository.findById(UUID.fromString(dto.getLocataireId())).orElseThrow());
        loc.setDateDebut(LocalDate.parse(dto.getDateDebut()));
        loc.setDateFin(dto.getDateFin()!=null? LocalDate.parse(dto.getDateFin()):null);
        loc.setLoyerMensuel(new BigDecimal(dto.getLoyerMensuel()));
        loc.setChargesMensuelles(new BigDecimal(dto.getChargesMensuelles()));
        loc.setDepotGarantie(new BigDecimal(dto.getDepotGarantie()));
        loc.setFrequenceLoyer(dto.getFrequenceLoyer());
        loc.setJourEcheance(Integer.parseInt(dto.getJourEcheance()));
        loc.setModifieLe(LocalDateTime.now());

        Location saved = locationRepository.save(loc);
        return toDto(saved);
    }

    public void deleteLocation(String id) {
        locationRepository.deleteById(UUID.fromString(id));
    }

    private LocationDTO toDto(Location loc) {
        LocationDTO dto = new LocationDTO();
        dto.setId(loc.getId().toString());
        dto.setProprieteId(loc.getPropriete().getId().toString());
        dto.setProprieteNom(loc.getPropriete().getNom());
        dto.setLocataireId(loc.getLocataire().getId().toString());
        dto.setLocataireNom(loc.getLocataire().getNom());
        dto.setLocatairePrenom("");
        dto.setDateDebut(loc.getDateDebut().toString());
        dto.setDateFin(loc.getDateFin() != null ? loc.getDateFin().toString() : null);
        dto.setLoyerMensuel(loc.getLoyerMensuel().toPlainString());
        dto.setChargesMensuelles(loc.getChargesMensuelles().toPlainString());
        dto.setDepotGarantie(loc.getDepotGarantie().toPlainString());
        dto.setFrequenceLoyer(loc.getFrequenceLoyer());
        dto.setJourEcheance(Integer.toString(loc.getJourEcheance()));
        return dto;
    }


    // --- QUITTANCE ---
    public List<QuittanceDTO> getQuittancesByUtilisateur(String utilisateurId) {
        UUID uid = UUID.fromString(utilisateurId);
        return quittanceRepository.findByLocation_Propriete_Utilisateur_Id(uid)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public QuittanceDTO saveQuittance(QuittanceDTO dto) {
        Quittance q;
        boolean isNew = false;
        if (dto.getId()!=null && !dto.getId().isEmpty()) {
            q = quittanceRepository.findById(UUID.fromString(dto.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Quittance introuvable"));
        } else {
            q = new Quittance();
            q.setId(UUID.randomUUID());
            q.setCreeLe(LocalDateTime.now());
            isNew = true;
        }
        q.setLocation(locationRepository.findById(UUID.fromString(dto.getLocationId())).orElseThrow());
        q.setDateDebut(LocalDate.parse(dto.getDateDebut()));
        q.setDateFin(dto.getDateFin()!=null?LocalDate.parse(dto.getDateFin()):null);
        //q.setDateEmission(LocalDate.parse(dto.getDateEmission()));
        q.setDateEcheance(dto.getDateEcheance()!=null?LocalDate.parse(dto.getDateEcheance()):null);
        q.setMontantLoyer(dto.getMontantLoyer()!=null ? new BigDecimal(dto.getMontantLoyer()) : BigDecimal.ZERO);
        q.setMontantCharges(dto.getMontantCharges()!=null ? new BigDecimal(dto.getMontantCharges()) : BigDecimal.ZERO);
        //q.setMontantTotal(dto.getMontantTotal()!=null ? new BigDecimal(dto.getMontantTotal()) : BigDecimal.ZERO);
        q.setStatut(Quittance.StatutQuittance.valueOf(dto.getStatut()));
        q.setInclureCaution(dto.getInclureCaution());
        q.setDepotGarantie(dto.getDepotGarantie()!=null ? new BigDecimal(dto.getDepotGarantie()) : BigDecimal.ZERO);
        q.setModifieLe(LocalDateTime.now());

        Quittance saved = quittanceRepository.save(q);
        
        // Créer automatiquement l'écriture comptable pour une nouvelle quittance
        if (isNew) {
            createEcritureComptableQuittance(saved.getId().toString());
        }
        
        return toDto(saved);
    }

    public void deleteQuittance(String id) {
        quittanceRepository.deleteById(UUID.fromString(id));
    }

    private QuittanceDTO toDto(Quittance q) {
        QuittanceDTO dto = new QuittanceDTO();
        dto.setId(q.getId().toString());
        dto.setLocationId(q.getLocation().getId().toString());
        dto.setDateDebut(q.getDateDebut().toString());
        dto.setDateFin(q.getDateFin()!=null?q.getDateFin().toString():null);
        //dto.setDateEmission(q.getDateEmission().toString());
        dto.setDateEcheance(q.getDateEcheance()!=null?q.getDateEcheance().toString():null);
        dto.setMontantLoyer(q.getMontantLoyer().toPlainString());
        dto.setMontantCharges(q.getMontantCharges().toPlainString());
        //dto.setMontantTotal(q.getMontantTotal().toPlainString());
        dto.setStatut(q.getStatut().toString());
        dto.setInclureCaution(q.getInclureCaution());
        dto.setDepotGarantie(q.getDepotGarantie()!=null?q.getDepotGarantie().toPlainString():null);
        // Ajout des noms propriété et locataire
        Location loc = q.getLocation();
        String proprieteNom = loc != null && loc.getPropriete() != null ? loc.getPropriete().getNom() : null;
        String locataireNom = loc != null && loc.getLocataire() != null ? loc.getLocataire().getNom() : null;
        dto.setProprieteNom(proprieteNom);
        dto.setLocataireNom(locataireNom);
        return dto;
    }

    // --- PAIEMENT ---
    public List<PaiementDTO> getPaiementsByUtilisateur(String utilisateurId) {
        UUID uid = UUID.fromString(utilisateurId);
        return paiementRepository.findByQuittance_Location_Propriete_Utilisateur_Id(uid)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PaiementDTO savePaiement(PaiementDTO dto) {
        Paiement p;
        if (dto.getId()!=null && !dto.getId().isEmpty()) {
            p = paiementRepository.findById(UUID.fromString(dto.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable"));
        } else {
            p = new Paiement();
            p.setId(UUID.randomUUID());
            p.setCreeLe(LocalDateTime.now());
        }
        p.setQuittance(quittanceRepository.findById(UUID.fromString(dto.getQuittanceId())).orElseThrow());
        p.setDatePaiement(LocalDate.parse(dto.getDatePaiement()));
        p.setMontant(new BigDecimal(dto.getMontant()));
        p.setMoyenPaiement(dto.getMoyenPaiement());
        p.setReference(dto.getReference());
        p.setCommentaire(dto.getCommentaire());
        p.setEstValide(Boolean.valueOf(dto.getEstValide()));
        p.setModifieLe(LocalDateTime.now());

        Paiement saved = paiementRepository.save(p);
        return toDTO(saved);
    }

    public void deletePaiement(String id) {
        paiementRepository.deleteById(UUID.fromString(id));
    }

    private PaiementDTO toDTO(Paiement p) {
        PaiementDTO dto = new PaiementDTO();
        dto.setId(p.getId().toString());
        dto.setQuittanceId(p.getQuittance().getId().toString());
        dto.setDatePaiement(p.getDatePaiement().toString());
        dto.setMontant(p.getMontant().toPlainString());
        dto.setMoyenPaiement(p.getMoyenPaiement().toString());
        dto.setReference(p.getReference());
        dto.setCommentaire(p.getCommentaire());
        dto.setEstValide(p.getEstValide().toString());
        return dto;
    }

    public List<PaiementDTO> getPaiementsByQuittance(String quittanceId) {
        UUID qid = UUID.fromString(quittanceId);
        return paiementRepository.findByQuittance_Id(qid)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    // --- CREDIT ---
    public List<CreditDTO> getCreditsByUtilisateur(String utilisateurId) {
        UUID uid = UUID.fromString(utilisateurId);
        return creditRepository.findByPropriete_Utilisateur_Id(uid)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CreditDTO saveCredit(CreditDTO dto) {
        Credit c;
        if (dto.getId()!=null && !dto.getId().isEmpty()) {
            c = creditRepository.findById(UUID.fromString(dto.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Crédit introuvable"));
        } else {
            c = new Credit();
            c.setId(UUID.randomUUID());
            c.setCreeLe(LocalDateTime.now());
        }
        c.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId())).orElseThrow());
        c.setBanque(dto.getBanque());
        c.setMontantEmprunte(new BigDecimal(dto.getMontantEmprunte()));
        c.setDateDebut(LocalDate.parse(dto.getDateDebut()));
        c.setDateFin(LocalDate.parse(dto.getDateFin()));
        c.setDureeMois(Integer.parseInt(dto.getDureeMois()));
        c.setTauxInteretAnnuel(new BigDecimal(dto.getTauxInteretAnnuel()));
        c.setMensualite(new BigDecimal(dto.getMensualite()));
        c.setAssuranceMensuelle(new BigDecimal(dto.getAssuranceMensuelle()));
        c.setFraisDossier(new BigDecimal(dto.getFraisDossier()));
        c.setFraisGarantie(new BigDecimal(dto.getFraisGarantie()));
        c.setModifieLe(LocalDateTime.now());

        Credit saved = creditRepository.save(c);
        return toDto(saved);
    }

    @Transactional
    public void deleteCredit(String id) {
        UUID creditId = UUID.fromString(id);
        echeanceCreditRepository.deleteByCreditId(creditId);
        creditRepository.deleteById(creditId);
    }

    private CreditDTO toDto(Credit c) {
        CreditDTO dto = new CreditDTO();
        dto.setId(c.getId().toString());
        dto.setProprieteId(c.getPropriete().getId().toString());
        dto.setBanque(c.getBanque());
        dto.setMontantEmprunte(c.getMontantEmprunte().toPlainString());
        dto.setDateDebut(c.getDateDebut().toString());
        dto.setDateFin(c.getDateFin().toString());
        dto.setDureeMois(c.getDureeMois().toString());
        dto.setTauxInteretAnnuel(c.getTauxInteretAnnuel().toPlainString());
        dto.setMensualite(c.getMensualite().toPlainString());
        dto.setAssuranceMensuelle(c.getAssuranceMensuelle().toPlainString());
        dto.setFraisDossier(c.getFraisDossier().toPlainString());
        dto.setFraisGarantie(c.getFraisGarantie().toPlainString());
        return dto;
    }


    public byte[] generateQuittancePdf(String quittanceId) {
        Quittance quittance = quittanceRepository.findById(UUID.fromString(quittanceId))
            .orElseThrow(() -> new RuntimeException("Quittance non trouvée"));

        Location location = quittance.getLocation();
        Locataire locataire = location.getLocataire();
        Propriete propriete = location.getPropriete();
        Utilisateur bailleur = propriete.getUtilisateur();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // --- LOGO ---
            try {
                // iText/Lowagie ne supporte pas nativement le SVG, mais on tente l'ajout (sinon ignorer)
                // Si besoin, convertir le SVG en PNG côté projet et utiliser le PNG ici
                Image logo = Image.getInstance("src/assets/svg-icon/logo.svg");
                logo.scaleToFit(120, 60);
                logo.setAlignment(Image.ALIGN_CENTER);
                document.add(logo);
            } catch (Exception e) {
                // Si le SVG n'est pas supporté, on continue sans logo
            }

            // --- TITRES ET COULEURS ---
            Font titreFont = new Font(Font.HELVETICA, 18, Font.BOLD, new java.awt.Color(0, 102, 204));
            Font sectionFont = new Font(Font.HELVETICA, 12, Font.BOLD, new java.awt.Color(0, 51, 102));
            Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL, java.awt.Color.BLACK);

            Paragraph titre = new Paragraph("QUITTANCE DE LOYER", titreFont);
            titre.setAlignment(Paragraph.ALIGN_CENTER);
            titre.setSpacingAfter(10);
            document.add(titre);

            document.add(new Paragraph("Date d'émission : " + (quittance.getDateEmission() != null ? quittance.getDateEmission() : java.time.LocalDate.now()), normalFont));
            document.add(new Paragraph(" "));

            // --- SÉPARATEUR ---
            document.add(new com.lowagie.text.pdf.draw.LineSeparator(1, 100, new java.awt.Color(0, 102, 204), com.lowagie.text.Element.ALIGN_CENTER, -2));

            // --- INFOS BAILLEUR ---
            document.add(new Paragraph("Bailleur", sectionFont));
            document.add(new Paragraph((bailleur.getPrenom() != null ? bailleur.getPrenom() + " " : "") + bailleur.getNom(), normalFont));
            document.add(new Paragraph("Email : " + bailleur.getEmail(), normalFont));
            document.add(new Paragraph(" "));

            // --- INFOS LOCATAIRE ---
            document.add(new Paragraph("Locataire", sectionFont));
            document.add(new Paragraph(locataire.getNom(), normalFont));
            document.add(new Paragraph("Adresse : " + locataire.getAdresse()
                + (locataire.getComplementAdresse() != null ? ", " + locataire.getComplementAdresse() : "")
                + ", " + locataire.getCodePostal() + " " + locataire.getVille(), normalFont));
            document.add(new Paragraph("Email : " + locataire.getEmail(), normalFont));
            document.add(new Paragraph("Téléphone : " + locataire.getTelephone(), normalFont));
            document.add(new Paragraph(" "));

            // --- INFOS BIEN ---
            document.add(new Paragraph("Bien loué", sectionFont));
            document.add(new Paragraph(propriete.getNom() + " (" + (propriete.getTypeBien() != null ? propriete.getTypeBien().toString() : "") + ")", normalFont));
            document.add(new Paragraph("Adresse : " + propriete.getAdresse()
                + (propriete.getComplementAdresse() != null ? ", " + propriete.getComplementAdresse() : "")
                + ", " + propriete.getCodePostal() + " " + propriete.getVille(), normalFont));
            document.add(new Paragraph(" "));

            // --- DÉTAILS LOCATION ---
            document.add(new Paragraph("Détails de la location", sectionFont));
            document.add(new Paragraph("Période : du " + quittance.getDateDebut() + " au " + quittance.getDateFin(), normalFont));
            document.add(new Paragraph("Loyer mensuel : " + quittance.getMontantLoyer() + " €", normalFont));
            document.add(new Paragraph("Charges mensuelles : " + quittance.getMontantCharges() + " €", normalFont));
            if (Boolean.TRUE.equals(quittance.getInclureCaution()) && quittance.getDepotGarantie() != null) {
                document.add(new Paragraph("Caution : " + quittance.getDepotGarantie() + " €", normalFont));
            }
            java.math.BigDecimal total = quittance.getMontantLoyer()
                .add(quittance.getMontantCharges())
                .add(Boolean.TRUE.equals(quittance.getInclureCaution()) && quittance.getDepotGarantie() != null ? quittance.getDepotGarantie() : java.math.BigDecimal.ZERO);
            document.add(new Paragraph("Total dû : " + total + " €", normalFont));
            document.add(new Paragraph("Statut : " + quittance.getStatut(), normalFont));
            if (quittance.getDateEcheance() != null) {
                document.add(new Paragraph("Date d'échéance : " + quittance.getDateEcheance(), normalFont));
            }
            document.add(new Paragraph(" "));

            // --- SÉPARATEUR ---
            document.add(new com.lowagie.text.pdf.draw.LineSeparator(1, 100, new java.awt.Color(0, 102, 204), com.lowagie.text.Element.ALIGN_CENTER, -2));

            // --- MENTION LÉGALE ---
            Font legalFont = new Font(Font.HELVETICA, 10, Font.ITALIC, new java.awt.Color(100, 100, 100));
            Paragraph legal = new Paragraph("La présente quittance atteste que le locataire s'est acquitté du montant dû pour la période indiquée.", legalFont);
            legal.setSpacingBefore(10);
            document.add(legal);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur de génération PDF : " + e.getMessage(), e);
        }
    }


    public DocumentDTO saveDocument(DocumentDTO dto) {
        DocumentEntity doc = new DocumentEntity();
        doc.setId(UUID.randomUUID());
        
        // Récupérer l'utilisateur
        if (dto.getUtilisateurId() != null && !dto.getUtilisateurId().isEmpty()) {
            doc.setUtilisateur(utilisateurRepository.findById(UUID.fromString(dto.getUtilisateurId()))
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé")));
        }
        
        // Récupérer la propriété si fournie
        if (dto.getProprieteId() != null && !dto.getProprieteId().isEmpty()) {
            doc.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId()))
                .orElse(null));
        }
        
        // Récupérer le locataire si fourni
        if (dto.getLocataireId() != null && !dto.getLocataireId().isEmpty()) {
            doc.setLocataire(locataireRepository.findById(UUID.fromString(dto.getLocataireId()))
                .orElse(null));
        }
        
        // Définir les champs de base
        if (dto.getTypeDocument() != null && !dto.getTypeDocument().isEmpty()) {
            try {
                // Convertir la chaîne en majuscules pour correspondre aux valeurs de l'énumération
                String typeDocumentStr = dto.getTypeDocument().toUpperCase();
                doc.setTypeDocument(DocumentEntity.TypeDocument.valueOf(typeDocumentStr));
            } catch (IllegalArgumentException e) {
                // Si la valeur n'est pas valide, utiliser AUTRE par défaut
                doc.setTypeDocument(DocumentEntity.TypeDocument.AUTRE);
            }
        }
        
        doc.setTitre(dto.getTitre());
        doc.setDateDocument(dto.getDateDocument() != null ? LocalDate.parse(dto.getDateDocument()) : LocalDate.now());
        
        // Encoder le contenu en base64 si ce n'est pas déjà fait
        String contenu = dto.getContenu();
/*         if (contenu != null && !contenu.isEmpty() && !contenu.matches("^[A-Za-z0-9+/=]+$")) {
            contenu = java.util.Base64.getEncoder().encodeToString(contenu.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } */
        doc.setContenu(contenu);
        
        doc.setMimeType(dto.getMimeType());
        doc.setNomFichier(dto.getNomFichier());
        doc.setTaille(dto.getTaille());
        
        // Métadonnées
        doc.setCreeLe(LocalDateTime.now());
        doc.setModifieLe(LocalDateTime.now());

        // Sauvegarder le document
        DocumentEntity savedDoc = documentEntityRepository.save(doc);
        return toDTO(savedDoc);
    }


private DocumentDTO toDTO(DocumentEntity document) {
    if (document == null) {
        return null;
    }
    
    DocumentDTO dto = new DocumentDTO();
    dto.setId(document.getId().toString());
    dto.setTitre(document.getTitre());
    
    if (document.getTypeDocument() != null) {
        dto.setTypeDocument(document.getTypeDocument().name());
    }
    
    dto.setDateDocument(document.getDateDocument() != null ? document.getDateDocument().toString() : null);
    dto.setContenu(document.getContenu());
    dto.setMimeType(document.getMimeType());
    dto.setNomFichier(document.getNomFichier());
    dto.setTaille(document.getTaille());

    // Références aux entités liées
    if (document.getUtilisateur() != null) {
        dto.setUtilisateurId(document.getUtilisateur().getId().toString());
    }
    if (document.getPropriete() != null) {
        dto.setProprieteId(document.getPropriete().getId().toString());
    }
    if (document.getLocataire() != null) {
        dto.setLocataireId(document.getLocataire().getId().toString());
    }
    
    return dto;
}



    @Transactional(readOnly = true)
    public DocumentDTO getDocumentMetadataById(String id) {
        DocumentEntity doc = documentEntityRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new RuntimeException("Document introuvable"));
            
        DocumentDTO dto = new DocumentDTO();
        dto.setId(doc.getId().toString());
        dto.setNomFichier(doc.getNomFichier());
        dto.setMimeType(doc.getMimeType());
        dto.setTaille(doc.getTaille());
        dto.setTypeDocument(doc.getTypeDocument() != null ? doc.getTypeDocument().name() : null);
        dto.setTitre(doc.getTitre());
        dto.setDateDocument(doc.getDateDocument() != null ? doc.getDateDocument().toString() : null);
        // Ne pas inclure le contenu pour les métadonnées
        return dto;
    }

    @Transactional(readOnly = true)
    public byte[] getDocumentContentById(String id) {
        DocumentEntity doc = documentEntityRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new RuntimeException("Document introuvable"));
            
        String contenu = doc.getContenu();
        if (contenu == null || contenu.trim().isEmpty()) {
            throw new RuntimeException("Contenu du document vide ou introuvable");
        }
        
        try {
            // Essayer de décoder en base64
            try {
                return java.util.Base64.getDecoder().decode(contenu);
            } catch (IllegalArgumentException e) {
                // Si le décodage échoue, retourner le contenu tel quel
                return contenu.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du contenu du document: " + e.getMessage(), e);
        }
    }

    @Deprecated
    public DocumentDTO getDocumentById(String id) {
        DocumentEntity doc = documentEntityRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new RuntimeException("Document introuvable"));
        return toDTO(doc);
    }

public List<DocumentDTO> getDocumentsByUtilisateur(String utilisateurId) {
    UUID userId = UUID.fromString(utilisateurId);
    return documentEntityRepository.findByUtilisateur_Id(userId).stream()
        .map(this::toDTO)
        .collect(Collectors.toList());
}

@Transactional
public void deleteDocument(String documentId) {
    try {
        UUID id = UUID.fromString(documentId);
        if (!documentEntityRepository.existsById(id)) {
            throw new RuntimeException("Document introuvable avec l'ID: " + documentId);
        }
        documentEntityRepository.deleteById(id);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("ID de document invalide: " + documentId, e);
    } catch (Exception e) {
        throw new RuntimeException("Erreur lors de la suppression du document: " + e.getMessage(), e);
    }
}
    
public CompositionAcquisitionDTO toDTO(CompositionAcquisition c) {
    CompositionAcquisitionDTO dto = new CompositionAcquisitionDTO();
    dto.setId(c.getId().toString());
    dto.setCategorie(c.getCategorie());
    dto.setMontant(c.getMontant().toPlainString());
    dto.setDescription(c.getDescription());
    return dto;
}

    @Transactional(readOnly = true)
    public ProprieteDetailDTO getProprieteDetail(UUID proprieteId) {
        Propriete propriete = proprieteRepository.findById(proprieteId)
            .orElseThrow(() -> new RuntimeException("Propriété non trouvée"));

        List<CompositionAcquisitionDTO> compositions = propriete.getCompositions().stream()
            .map(this::toDTO)
            .toList();


        // Récupérer uniquement les métadonnées des documents, pas le contenu
        List<DocumentDTO> documents = documentEntityRepository.findByPropriete_Id(proprieteId).stream()
            .map(doc -> {
                DocumentDTO dto = new DocumentDTO();
                dto.setId(doc.getId().toString());
                dto.setTitre(doc.getTitre());
                dto.setTypeDocument(doc.getTypeDocument() != null ? doc.getTypeDocument().name() : null);
                dto.setDateDocument(doc.getDateDocument() != null ? doc.getDateDocument().toString() : null);
                dto.setNomFichier(doc.getNomFichier());
                dto.setMimeType(doc.getMimeType());
                dto.setTaille(doc.getTaille());
                // Ne pas inclure le contenu
                return dto;
            })
            .toList();

        return new ProprieteDetailDTO(
            toDTO(propriete),
            compositions,
            documents
        );
    }

    
    public DashboardDTO getDashboard(String id) {
        UUID utilisateurId = UUID.fromString(id);
        List<Propriete> proprietes = proprieteRepository.findByUtilisateur_Id(utilisateurId);
        List<Location> locations = locationRepository.findByPropriete_Utilisateur_Id(utilisateurId);
        List<Quittance> quittances = quittanceRepository.findByLocation_Propriete_Utilisateur_Id(utilisateurId);
        List<Paiement> paiements = paiementRepository.findByQuittance_Location_Propriete_Utilisateur_Id(utilisateurId);

        long totalBiens = proprietes.size();

        // Répartition par type
        Map<String, Long> repartitionParType = proprietes.stream()
            .collect(Collectors.groupingBy(
                p -> p.getTypeBien().toString(),
            Collectors.counting()
        ));

    // Taux d'occupation = nb locations actives / nb propriétés
    long nbLocationsActives = locations.stream()
        .filter(loc -> loc.getDateFin() == null || loc.getDateFin().isAfter(LocalDate.now()))
        .count();
    double tauxOccupation = totalBiens == 0 ? 0.0 : (double) nbLocationsActives / totalBiens;

    // Alerte impayé = quittances non soldées
    List<Quittance> impayes = quittances.stream()
        .filter(q -> q.getStatut() != null && (q.getStatut() == Quittance.StatutQuittance.IMPAYEE || q.getStatut() == Quittance.StatutQuittance.PARTIELLE))
        .toList();

    // Total des loyers perçus ce mois-ci
    LocalDate premierJourMois = LocalDate.now().withDayOfMonth(1);
    LocalDate dernierJourMois = premierJourMois.with(TemporalAdjusters.lastDayOfMonth());
    BigDecimal totalLoyersPerçus = paiements.stream()
        .filter(p -> {
            LocalDate dp = p.getDatePaiement();
            return dp != null && !dp.isBefore(premierJourMois) && !dp.isAfter(dernierJourMois);
        })
        .map(Paiement::getMontant)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Répartition des loyers perçus mois par mois
    Map<String, BigDecimal> loyersMensuels = paiements.stream()
        .filter(p -> p.getDatePaiement() != null && p.getMontant() != null)
        .collect(Collectors.groupingBy(
            p -> p.getDatePaiement().format(DateTimeFormatter.ofPattern("yyyy-MM")),
            TreeMap::new, // pour les trier dans l'ordre chronologique
            Collectors.reducing(BigDecimal.ZERO, Paiement::getMontant, BigDecimal::add)
        ));

    DashboardDTO dto = new DashboardDTO();
    dto.setNombreBiens(totalBiens);
    dto.setRepartitionParType(repartitionParType);
    dto.setTauxOccupation(tauxOccupation);
    dto.setNombreImpayes(impayes.size());
    dto.setTotalLoyersPercus(totalLoyersPerçus);
    dto.setLoyersMensuels(loyersMensuels);

    return dto;
}



public LocataireDetailDTO getLocataireDetail(String locataireId) {
    Locataire locataire = locataireRepository.findById(UUID.fromString(locataireId))
        .orElseThrow(() -> new RuntimeException("Locataire introuvable"));

    // Documents liés
    List<DocumentDTO> documents = documentEntityRepository.findByLocataire_Id(locataire.getId()).stream()
        .map(this::toDTO)
        .collect(Collectors.toList());

    // Construction DTO
    LocataireDetailDTO dto = new LocataireDetailDTO();
    dto.setId(locataire.getId().toString());
    dto.setNom(locataire.getNom());
    dto.setTelephone(locataire.getTelephone());
    dto.setEmail(locataire.getEmail());
    dto.setAdresse(locataire.getAdresse());
    dto.setComplementAdresse(locataire.getComplementAdresse());
    dto.setCodePostal(locataire.getCodePostal());
    dto.setVille(locataire.getVille());
    dto.setDocuments(documents);

    return dto;
}

@Transactional
public LocationDetailDTO getLocationDetail(String locationId) {
    Location location = locationRepository.findById(UUID.fromString(locationId))
        .orElseThrow(() -> new IllegalArgumentException("Location non trouvée"));

    LocationDetailDTO dto = new LocationDetailDTO();
    dto.setId(location.getId().toString());
    dto.setDateDebut(location.getDateDebut() != null ? location.getDateDebut().toString() : null);
    dto.setDateFin(location.getDateFin() != null ? location.getDateFin().toString() : null);
    dto.setFrequenceLoyer(location.getFrequenceLoyer());
    dto.setJourEcheance(location.getJourEcheance() != null ? location.getJourEcheance().toString() : null);
    dto.setLoyerMensuel(location.getLoyerMensuel() != null ? location.getLoyerMensuel().toString() : null);
    dto.setChargesMensuelles(location.getChargesMensuelles() != null ? location.getChargesMensuelles().toString() : null);
    dto.setDepotGarantie(location.getDepotGarantie() != null ? location.getDepotGarantie().toString() : null);

    dto.setLocataire(toDTO(location.getLocataire()));
    dto.setPropriete(toDTO(location.getPropriete()));

    return dto;
}


public LocataireDTO toDTO(Locataire locataire) {
    if (locataire == null) return null;

    LocataireDTO dto = new LocataireDTO();
    dto.setId(locataire.getId().toString());
    dto.setUtilisateurId(locataire.getUtilisateur() != null ? locataire.getUtilisateur().getId().toString() : null);
    dto.setNom(locataire.getNom());
    dto.setTelephone(locataire.getTelephone());
    dto.setEmail(locataire.getEmail());
    dto.setAdresse(locataire.getAdresse());
    dto.setComplementAdresse(locataire.getComplementAdresse());
    dto.setCodePostal(locataire.getCodePostal());
    dto.setVille(locataire.getVille());

    return dto;
}

public LocataireDetailDTO getLocataireDetails(UUID locataireId) {
    Locataire locataire = locataireRepository.findById(locataireId)
        .orElseThrow(() -> new RuntimeException("Locataire non trouvé"));

    LocataireDTO locataireDTO = toDTO(locataire);
    List<DocumentDTO> documents = documentEntityRepository.findByLocataire_Id(locataireId).stream()
        .map(this::toDTO)
        .toList();

    LocataireDetailDTO detailDTO = new LocataireDetailDTO();
    detailDTO.setId(locataireDTO.getId());
    detailDTO.setNom(locataireDTO.getNom());
    detailDTO.setTelephone(locataireDTO.getTelephone());
    detailDTO.setEmail(locataireDTO.getEmail());
    detailDTO.setAdresse(locataireDTO.getAdresse());
    detailDTO.setComplementAdresse(locataireDTO.getComplementAdresse());
    detailDTO.setCodePostal(locataireDTO.getCodePostal());
    detailDTO.setVille(locataireDTO.getVille());
    detailDTO.setDocuments(documents);
    return detailDTO;
}

public QuittanceDTO getQuittanceById(String id) {
    return quittanceRepository.findById(UUID.fromString(id))
        .map(this::toDto)
        .orElse(null);
}

public List<QuittanceDTO> getQuittancesByLocation(String locationId) {
    return quittanceRepository.findByLocation_Id(UUID.fromString(locationId))
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
}

// ===== SERVICES POUR LES IMMOBILISATIONS =====

public List<ImmobilisationDTO> getImmobilisationsByUtilisateur(String utilisateurId) {
    UUID uid = UUID.fromString(utilisateurId);
    return immobilisationRepository.findByUtilisateurId(uid)
            .stream()
            .map(ImmobilisationDTO::fromEntity)
            .collect(Collectors.toList());
}

public List<ImmobilisationDTO> getImmobilisationsByPropriete(String proprieteId) {
    UUID pid = UUID.fromString(proprieteId);
    return immobilisationRepository.findByProprieteId(pid)
            .stream()
            .map(ImmobilisationDTO::fromEntity)
            .collect(Collectors.toList());
}

public ImmobilisationDTO saveImmobilisation(ImmobilisationDTO dto) {
    Immobilisation i;
    if (dto.getId() != null && !dto.getId().isEmpty()) {
        i = immobilisationRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Immobilisation introuvable"));
    } else {
        i = new Immobilisation();
        i.setCreeLe(LocalDateTime.now());
    }

    i.setIntitule(dto.getIntitule());
    i.setMontant(new BigDecimal(dto.getMontant()));
    i.setDateAcquisition(LocalDate.parse(dto.getDateAcquisition()));
    i.setDureeAmortissement(Integer.valueOf(dto.getDureeAmortissement()));
    i.setTypeImmobilisation(dto.getTypeImmobilisation());
    i.setCategorieFiscale(Immobilisation.CategorieFiscale.valueOf(dto.getCategorieFiscale()));
    
    if (dto.getValeurTerrain() != null && !dto.getValeurTerrain().isEmpty()) {
        i.setValeurTerrain(new BigDecimal(dto.getValeurTerrain()));
    }
    
    i.setCommentaire(dto.getCommentaire());
    i.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId())).orElseThrow());
    i.setUtilisateur(utilisateurRepository.findById(UUID.fromString(dto.getUtilisateurId())).orElseThrow());
    i.setModifieLe(LocalDateTime.now());

    Immobilisation saved = immobilisationRepository.save(i);
    
    // Générer automatiquement le plan d'amortissement
    try {
        genererPlanAmortissement(saved.getId().toString());
        System.out.println("Plan d'amortissement généré avec succès pour l'immobilisation: " + saved.getId());
    } catch (Exception e) {
        System.err.println("Erreur lors de la génération du plan d'amortissement: " + e.getMessage());
        e.printStackTrace();
        // Ne pas faire échouer la sauvegarde de l'immobilisation si la génération du plan échoue
    }
    
    return ImmobilisationDTO.fromEntity(saved);
}

public void deleteImmobilisation(String id) {
    // Supprimer les amortissements associés
    List<Amortissement> amortissements = amortissementRepository.findByImmobilisationId(UUID.fromString(id));
    amortissementRepository.deleteAll(amortissements);
    
    // Supprimer l'immobilisation
    immobilisationRepository.deleteById(UUID.fromString(id));
}

// ===== SERVICES POUR LES AMORTISSEMENTS =====

public List<AmortissementDTO> getAmortissementsByUtilisateur(String utilisateurId) {
    UUID uid = UUID.fromString(utilisateurId);
    return amortissementRepository.findByUtilisateurId(uid)
            .stream()
            .map(AmortissementDTO::fromEntity)
            .collect(Collectors.toList());
}

public List<AmortissementDTO> getAmortissementsByPropriete(String proprieteId) {
    UUID pid = UUID.fromString(proprieteId);
    return amortissementRepository.findByProprieteId(pid)
            .stream()
            .map(AmortissementDTO::fromEntity)
            .collect(Collectors.toList());
}

public List<AmortissementDTO> getAmortissementsByImmobilisation(String immobilisationId) {
    UUID iid = UUID.fromString(immobilisationId);
    return amortissementRepository.findByImmobilisationId(iid)
            .stream()
            .map(AmortissementDTO::fromEntity)
            .collect(Collectors.toList());
}

public List<AmortissementDTO> getAmortissementsByUtilisateurAndAnnee(String utilisateurId, int annee) {
    UUID uid = UUID.fromString(utilisateurId);
    return amortissementRepository.findByUtilisateurIdAndAnnee(uid, annee)
            .stream()
            .map(AmortissementDTO::fromEntity)
            .collect(Collectors.toList());
}

@Transactional
public void genererPlanAmortissement(String immobilisationId) {
    System.out.println("=== GÉNÉRATION PLAN AMORTISSEMENT ===");
    System.out.println("Immobilisation ID: " + immobilisationId);
    
    Immobilisation immobilisation = immobilisationRepository.findById(UUID.fromString(immobilisationId))
            .orElseThrow(() -> new IllegalArgumentException("Immobilisation introuvable"));
    
    System.out.println("Immobilisation trouvée: " + immobilisation.getIntitule());
    
    // Supprimer les anciens amortissements
    List<Amortissement> anciensAmortissements = amortissementRepository.findByImmobilisationId(immobilisation.getId());
    System.out.println("Anciens amortissements à supprimer: " + anciensAmortissements.size());
    amortissementRepository.deleteAll(anciensAmortissements);
    
    // Calculer la base amortissable (montant - valeur terrain pour les biens immobiliers)
    BigDecimal baseAmortissable = immobilisation.getMontant();
    if ("BIEN_IMMOBILIER".equals(immobilisation.getTypeImmobilisation()) && immobilisation.getValeurTerrain() != null) {
        baseAmortissable = baseAmortissable.subtract(immobilisation.getValeurTerrain());
    }
    
    System.out.println("Base amortissable: " + baseAmortissable);
    
    // Calculer le taux d'amortissement
    BigDecimal tauxAnnuel = BigDecimal.valueOf(100.0)
            .divide(BigDecimal.valueOf(immobilisation.getDureeAmortissement()), 2, BigDecimal.ROUND_HALF_UP);
    
    System.out.println("Taux annuel: " + tauxAnnuel + "%");
    
    // Calculer l'annuité d'amortissement
    BigDecimal annuite = baseAmortissable
            .multiply(tauxAnnuel)
            .divide(BigDecimal.valueOf(100.0), 2, BigDecimal.ROUND_HALF_UP);
    
    System.out.println("Annuité: " + annuite);
    
    // Générer le plan d'amortissement
    BigDecimal valeurResiduelle = baseAmortissable;
    BigDecimal cumulAmortissements = BigDecimal.ZERO;
    
    for (int annee = 1; annee <= immobilisation.getDureeAmortissement(); annee++) {
        Amortissement amortissement = new Amortissement();
        amortissement.setImmobilisation(immobilisation);
        amortissement.setAnnee(immobilisation.getDateAcquisition().getYear() + annee - 1);
        amortissement.setMontantAmortissement(annuite);
        amortissement.setTauxAmortissement(tauxAnnuel);
        
        cumulAmortissements = cumulAmortissements.add(annuite);
        amortissement.setCumulAmortissements(cumulAmortissements);
        
        valeurResiduelle = baseAmortissable.subtract(cumulAmortissements);
        amortissement.setValeurResiduelle(valeurResiduelle.max(BigDecimal.ZERO));
        
        amortissement.setCreeLe(LocalDateTime.now());
        amortissement.setModifieLe(LocalDateTime.now());
        
        Amortissement saved = amortissementRepository.save(amortissement);
        System.out.println("Amortissement créé pour l'année " + amortissement.getAnnee() + ": " + saved.getId());
    }
    
    System.out.println("Plan d'amortissement généré avec succès");
}

// ===== SERVICES POUR LES CHARGES =====

public List<ChargeDTO> getChargesByUtilisateur(String utilisateurId) {
    UUID uid = UUID.fromString(utilisateurId);
    return chargeRepository.findByUtilisateurId(uid)
            .stream()
            .map(this::chargeToDTO)
            .collect(Collectors.toList());
}

public List<ChargeDTO> getChargesByPropriete(String proprieteId) {
    UUID pid = UUID.fromString(proprieteId);
    return chargeRepository.findByProprieteId(pid)
            .stream()
            .map(this::chargeToDTO)
            .collect(Collectors.toList());
}

public ChargeDTO saveCharge(ChargeDTO dto) {
    Charge c;
    if (dto.getId() != null && !dto.getId().isEmpty()) {
        c = chargeRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Charge introuvable"));
    } else {
        c = new Charge();
        c.setCreeLe(LocalDateTime.now());
    }

    c.setIntitule(dto.getIntitule());
    c.setMontant(new BigDecimal(dto.getMontant()));
    c.setDateCharge(LocalDate.parse(dto.getDateCharge()));
    c.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId())).orElseThrow());
    c.setNature(dto.getNature());
    c.setCommentaire(dto.getCommentaire());
    c.setUtilisateur(utilisateurRepository.findById(UUID.fromString(dto.getUtilisateurId())).orElseThrow());
    
    // Gérer le document associé
    if (dto.getDocumentId() != null && !dto.getDocumentId().isEmpty()) {
        c.setDocument(documentEntityRepository.findById(UUID.fromString(dto.getDocumentId())).orElse(null));
    } else {
        c.setDocument(null);
    }
    
    c.setModifieLe(LocalDateTime.now());

    Charge saved = chargeRepository.save(c);
    
    // Créer automatiquement l'écriture comptable pour une nouvelle charge
    if (dto.getId() == null || dto.getId().isEmpty()) {
        createEcritureComptableCharge(saved.getId().toString());
    }
    
    return chargeToDTO(saved);
}

public void deleteCharge(String id) {
    // Supprimer l'écriture comptable associée s'il y en a une
    EcritureComptable ecriture = ecritureComptableRepository.findByChargeId(UUID.fromString(id));
    if (ecriture != null) {
        ecritureComptableRepository.deleteById(ecriture.getId());
    }
    chargeRepository.deleteById(UUID.fromString(id));
}

private ChargeDTO chargeToDTO(Charge c) {
    return ChargeDTO.fromEntity(c);
}

// ===== SERVICES POUR LES RECETTES =====

public List<RecetteDTO> getRecettesByUtilisateur(String utilisateurId) {
    UUID uid = UUID.fromString(utilisateurId);
    return recetteRepository.findByUtilisateurId(uid)
            .stream()
            .map(this::recetteToDTO)
            .collect(Collectors.toList());
}

public List<RecetteDTO> getRecettesByPropriete(String proprieteId) {
    UUID pid = UUID.fromString(proprieteId);
    return recetteRepository.findByProprieteId(pid)
            .stream()
            .map(this::recetteToDTO)
            .collect(Collectors.toList());
}

public RecetteDTO saveRecette(RecetteDTO dto) {
    Recette r;
    if (dto.getId() != null && !dto.getId().isEmpty()) {
        r = recetteRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Recette introuvable"));
        // Valeur par défaut pour le type si non fourni
        if (dto.getType() == null || dto.getType().trim().isEmpty()) {
            dto.setType(r.getType());
        }
        // Valeur par défaut pour la propriété si non fournie
        if (dto.getProprieteId() == null || dto.getProprieteId().trim().isEmpty()) {
            dto.setProprieteId(r.getPropriete() != null ? r.getPropriete().getId().toString() : null);
        }
    } else {
        r = new Recette();
        r.setCreeLe(LocalDateTime.now());
    }

    r.setIntitule(dto.getIntitule());
    r.setMontant(new BigDecimal(dto.getMontant()));
    r.setDateRecette(LocalDate.parse(dto.getDateRecette()));
    r.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId())).orElseThrow());
    r.setType(dto.getType());
    
    if (dto.getQuittanceId() != null && !dto.getQuittanceId().isEmpty()) {
        r.setQuittance(quittanceRepository.findById(UUID.fromString(dto.getQuittanceId())).orElse(null));
    }
    
    r.setCommentaire(dto.getCommentaire());
    r.setUtilisateur(utilisateurRepository.findById(UUID.fromString(dto.getUtilisateurId())).orElseThrow());
    
    // Gérer le document associé
    if (dto.getDocumentId() != null && !dto.getDocumentId().isEmpty()) {
        r.setDocument(documentEntityRepository.findById(UUID.fromString(dto.getDocumentId())).orElse(null));
    } else {
        r.setDocument(null);
    }
    
    r.setModifieLe(LocalDateTime.now());

    Recette saved = recetteRepository.save(r);
    
    // Créer automatiquement l'écriture comptable pour une nouvelle recette
    if (dto.getId() == null || dto.getId().isEmpty()) {
        createEcritureComptableRecette(saved.getId().toString());
    }
    
    return recetteToDTO(saved);
}

public void deleteRecette(String id) {
    // Supprimer l'écriture comptable associée s'il y en a une
    EcritureComptable ecriture = ecritureComptableRepository.findByRecetteId(UUID.fromString(id));
    if (ecriture != null) {
        ecritureComptableRepository.deleteById(ecriture.getId());
    }
    recetteRepository.deleteById(UUID.fromString(id));
}

private RecetteDTO recetteToDTO(Recette r) {
    return RecetteDTO.fromEntity(r);
}

// ===== SERVICES POUR LES ÉCRITURES COMPTABLES =====

public List<EcritureComptableDTO> getEcrituresComptables(String proprieteId, int anneeFiscale) {
    UUID pid = UUID.fromString(proprieteId);
    return ecritureComptableRepository.findByProprieteIdAndAnnee(pid, anneeFiscale)
            .stream()
            .map(this::ecritureToDTO)
            .collect(Collectors.toList());
}

public List<EcritureComptableDTO> getEcrituresComptablesByUtilisateur(String utilisateurId) {
    UUID uid = UUID.fromString(utilisateurId);
    return ecritureComptableRepository.findByUtilisateurId(uid)
            .stream()
            .map(this::ecritureToDTO)
            .collect(Collectors.toList());
}

public List<EcritureComptableDTO> getEcrituresComptablesByUtilisateurAndAnnee(String utilisateurId, int anneeFiscale) {
    UUID uid = UUID.fromString(utilisateurId);
    return ecritureComptableRepository.findByUtilisateurIdAndAnnee(uid, anneeFiscale)
            .stream()
            .map(this::ecritureToDTO)
            .collect(Collectors.toList());
}

public EcritureComptableDTO createEcritureComptableCharge(String chargeId) {
    Charge charge = chargeRepository.findById(UUID.fromString(chargeId))
            .orElseThrow(() -> new IllegalArgumentException("Charge introuvable"));

    EcritureComptable ecriture = new EcritureComptable();
    ecriture.setDateEcriture(charge.getDateCharge());
    ecriture.setMontant(charge.getMontant());
    ecriture.setType(EcritureComptable.TypeEcriture.CHARGE);
    ecriture.setPropriete(charge.getPropriete());
    ecriture.setCharge(charge);
    ecriture.setCommentaire("Charge: " + charge.getIntitule());
    ecriture.setUtilisateur(charge.getUtilisateur());
    ecriture.setDocument(charge.getDocument());
    ecriture.setCreeLe(LocalDateTime.now());
    ecriture.setModifieLe(LocalDateTime.now());

    EcritureComptable saved = ecritureComptableRepository.save(ecriture);
    return ecritureToDTO(saved);
}

public EcritureComptableDTO createEcritureComptableRecette(String recetteId) {
    Recette recette = recetteRepository.findById(UUID.fromString(recetteId))
            .orElseThrow(() -> new IllegalArgumentException("Recette introuvable"));

    EcritureComptable ecriture = new EcritureComptable();
    ecriture.setDateEcriture(recette.getDateRecette());
    ecriture.setMontant(recette.getMontant());
    ecriture.setType(EcritureComptable.TypeEcriture.RECETTE);
    ecriture.setPropriete(recette.getPropriete());
    ecriture.setRecette(recette);
    ecriture.setCommentaire("Recette: " + recette.getIntitule());
    ecriture.setUtilisateur(recette.getUtilisateur());
    ecriture.setDocument(recette.getDocument());
    ecriture.setCreeLe(LocalDateTime.now());
    ecriture.setModifieLe(LocalDateTime.now());

    EcritureComptable saved = ecritureComptableRepository.save(ecriture);
    return ecritureToDTO(saved);
}

public EcritureComptableDTO createEcritureComptableQuittance(String quittanceId) {
    System.out.println("Début de création d'écriture comptable pour quittance: " + quittanceId);
    
    Quittance quittance = quittanceRepository.findById(UUID.fromString(quittanceId))
            .orElseThrow(() -> new IllegalArgumentException("Quittance introuvable avec l'ID: " + quittanceId));

    System.out.println("Quittance trouvée: " + quittance.getId() + " - Locataire: " + quittance.getLocation().getLocataire().getNom());

    // Vérifier si une écriture comptable existe déjà pour cette quittance
    // (via la recette liée à la quittance)
    List<Recette> recettesExistentes = recetteRepository.findByQuittanceId(quittance.getId());
    System.out.println("Recettes existantes trouvées: " + recettesExistentes.size());
    
    if (!recettesExistentes.isEmpty()) {
        // Une recette existe déjà, vérifier si elle a une écriture comptable
        for (Recette recetteExistante : recettesExistentes) {
            EcritureComptable ecritureExistante = ecritureComptableRepository.findByRecetteId(recetteExistante.getId());
            if (ecritureExistante != null) {
                System.out.println("Écriture comptable existante trouvée pour recette: " + recetteExistante.getId());
                throw new IllegalArgumentException("Une écriture comptable existe déjà pour cette quittance");
            }
        }
    }

    // Calculer le montant total de la quittance
    BigDecimal montantTotal = quittance.getMontantLoyer()
            .add(quittance.getMontantCharges())
            .add(Boolean.TRUE.equals(quittance.getInclureCaution()) && quittance.getDepotGarantie() != null 
                ? quittance.getDepotGarantie() : BigDecimal.ZERO);

    System.out.println("Montant total calculé: " + montantTotal);

        // Créer d'abord une recette automatiquement
        Recette recette = new Recette();
        // Ne pas définir l'ID manuellement, laisser Hibernate le gérer
        recette.setIntitule("Loyer - " + quittance.getLocation().getLocataire().getNom() + 
                           " - Période: " + quittance.getDateDebut() + " à " + 
                           (quittance.getDateFin() != null ? quittance.getDateFin() : "en cours"));
        recette.setMontant(montantTotal);
        recette.setDateRecette(quittance.getDateDebut());
        recette.setPropriete(quittance.getLocation().getPropriete());
        recette.setType(quittance.getLocation().getPropriete().getTypeBien().toString());
        recette.setQuittance(quittance); // Lier la recette à la quittance
        recette.setCommentaire("Recette générée automatiquement depuis la quittance #" + quittance.getId());
        recette.setUtilisateur(quittance.getLocation().getPropriete().getUtilisateur());
        recette.setCreeLe(LocalDateTime.now());
        recette.setModifieLe(LocalDateTime.now());

        System.out.println("Sauvegarde de la recette...");
        Recette savedRecette = recetteRepository.save(recette);
        System.out.println("Recette sauvegardée avec ID: " + savedRecette.getId());

        // Créer ensuite l'écriture comptable liée à cette recette
        EcritureComptable ecriture = new EcritureComptable();
        ecriture.setDateEcriture(quittance.getDateDebut());
        ecriture.setMontant(montantTotal);
        ecriture.setType(EcritureComptable.TypeEcriture.RECETTE);
        ecriture.setPropriete(quittance.getLocation().getPropriete());
        ecriture.setRecette(savedRecette);
        ecriture.setCommentaire("Quittance de loyer - " + quittance.getLocation().getLocataire().getNom() + 
                               " - Période: " + quittance.getDateDebut() + " à " + 
                               (quittance.getDateFin() != null ? quittance.getDateFin() : "en cours"));
        ecriture.setUtilisateur(quittance.getLocation().getPropriete().getUtilisateur());
        ecriture.setCreeLe(LocalDateTime.now());
        ecriture.setModifieLe(LocalDateTime.now());

        System.out.println("Sauvegarde de l'écriture comptable...");
        EcritureComptable saved = ecritureComptableRepository.save(ecriture);
        System.out.println("Écriture comptable sauvegardée avec ID: " + saved.getId());
        
        return ecritureToDTO(saved);
    }

    public EcritureComptableDTO updateEcritureComptable(EcritureComptableDTO dto) {
        EcritureComptable ecriture = ecritureComptableRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Écriture comptable introuvable"));

        ecriture.setDateEcriture(LocalDate.parse(dto.getDateEcriture()));
        ecriture.setMontant(new BigDecimal(dto.getMontant()));
        ecriture.setCommentaire(dto.getCommentaire());
        ecriture.setModifieLe(LocalDateTime.now());

        EcritureComptable saved = ecritureComptableRepository.save(ecriture);
        return ecritureToDTO(saved);
    }

    public void deleteEcritureComptable(String id) {
        EcritureComptable ecriture = ecritureComptableRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Écriture comptable introuvable"));
        
        // Supprimer aussi la recette associée si elle existe
        if (ecriture.getRecette() != null) {
            recetteRepository.deleteById(ecriture.getRecette().getId());
        }
        
        ecritureComptableRepository.deleteById(UUID.fromString(id));
    }

    private EcritureComptableDTO ecritureToDTO(EcritureComptable e) {
        return EcritureComptableDTO.fromEntity(e);
    }

    public RecetteDTO getRecetteById(String id) {
        return recetteRepository.findById(UUID.fromString(id))
            .map(this::recetteToDTO)
            .orElse(null);
    }

    public ChargeDTO getChargeById(String id) {
        return chargeRepository.findById(UUID.fromString(id))
            .map(this::chargeToDTO)
            .orElse(null);
    }

    public EcheanceCreditDTO saveEcheanceCredit(EcheanceCreditDTO dto) {
        EcheanceCredit e;
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            e = echeanceCreditRepository.findById(UUID.fromString(dto.getId())).orElse(new EcheanceCredit());
        } else {
            e = new EcheanceCredit();
            e.setId(UUID.randomUUID());
        }
        e.setCredit(creditRepository.findById(UUID.fromString(dto.getCreditId())).orElseThrow());
        e.setDateEcheance(LocalDate.parse(dto.getDateEcheance()));
        e.setInteret(new BigDecimal(dto.getInteret()));
        e.setCapitalRembourse(new BigDecimal(dto.getCapitalRembourse()));
        e.setAssurance(new BigDecimal(dto.getAssurance()));
        e.setTotalEcheance(new BigDecimal(dto.getTotalEcheance()));
        EcheanceCredit saved = echeanceCreditRepository.save(e);
        return toEcheanceCreditDto(saved);
    }

    private EcheanceCreditDTO toEcheanceCreditDto(EcheanceCredit e) {
        EcheanceCreditDTO dto = new EcheanceCreditDTO();
        dto.setId(e.getId() != null ? e.getId().toString() : null);
        dto.setCreditId(e.getCredit() != null ? e.getCredit().getId().toString() : null);
        dto.setDateEcheance(e.getDateEcheance() != null ? e.getDateEcheance().toString() : null);
        dto.setInteret(e.getInteret() != null ? e.getInteret().toPlainString() : null);
        dto.setCapitalRembourse(e.getCapitalRembourse() != null ? e.getCapitalRembourse().toPlainString() : null);
        dto.setAssurance(e.getAssurance() != null ? e.getAssurance().toPlainString() : null);
        dto.setTotalEcheance(e.getTotalEcheance() != null ? e.getTotalEcheance().toPlainString() : null);
        return dto;
    }

    public List<EcheanceCreditDTO> getEcheancesByCredit(String creditId) {
        UUID cid = UUID.fromString(creditId);
        return echeanceCreditRepository.findByCredit_Id(cid)
            .stream()
            .map(this::toEcheanceCreditDto)
            .collect(java.util.stream.Collectors.toList());
    }

    public List<Paiement> findPaiementsByQuittanceId(String quittanceId) {
        return paiementRepository.findByQuittance_Id(UUID.fromString(quittanceId));
    }

    public ResultatFiscalDTO calculerResultatFiscal(int annee, List<String> proprieteIds, String utilisateurId) {
        UUID userUuid = UUID.fromString(utilisateurId);
        List<UUID> propUuids = proprieteIds.stream().map(UUID::fromString).collect(Collectors.toList());

        // Calculs des totaux (déjà existant)
        double totalRecettes = recetteRepository.sumRecettesByYearAndProprietes(userUuid, annee, propUuids);
        double totalCharges = chargeRepository.sumChargesByYearAndProprietes(userUuid, annee, propUuids);
        double totalAmortissements = amortissementRepository.sumAmortissementsByYearAndProprietes(userUuid, annee, propUuids);
        double resultatFiscal = totalRecettes - totalCharges - totalAmortissements;

        // Récupération des listes détaillées
        List<Recette> recettes = recetteRepository.findByYearAndProprietes(userUuid, annee, propUuids);
        List<Charge> charges = chargeRepository.findByYearAndProprietes(userUuid, annee, propUuids);
        List<Amortissement> amortissements = amortissementRepository.findByYearAndProprietes(userUuid, annee, propUuids);

        // Conversion en DTOs de détail
        List<RecetteDetailDTO> recettesDetail = recettes.stream()
            .map(r -> new RecetteDetailDTO(r.getIntitule(), r.getDateRecette(), r.getMontant(), r.getPropriete().getNom()))
            .collect(Collectors.toList());

        List<ChargeDetailDTO> chargesDetail = charges.stream()
            .map(c -> new ChargeDetailDTO(c.getIntitule(), c.getDateCharge(), c.getMontant(), c.getPropriete().getNom(), c.getNature()))
            .collect(Collectors.toList());
            
        List<AmortissementDetailDTO> amortissementsDetail = amortissements.stream()
            .map(a -> new AmortissementDetailDTO(a.getImmobilisation().getIntitule(), a.getAnnee(), a.getMontantAmortissement(), a.getImmobilisation().getPropriete().getNom()))
            .collect(Collectors.toList());

        // Création du DTO final
        return new ResultatFiscalDTO(totalRecettes, totalCharges, totalAmortissements, resultatFiscal, recettesDetail, chargesDetail, amortissementsDetail);
    }




    }





