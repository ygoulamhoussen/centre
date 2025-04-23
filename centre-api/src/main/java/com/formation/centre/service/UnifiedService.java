package com.formation.centre.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.centre.dto.CompositionAcquisitionDTO;
import com.formation.centre.dto.LocataireDTO;
import com.formation.centre.dto.LocationDTO;
import com.formation.centre.dto.ProprieteDTO;
import com.formation.centre.model.CompositionAcquisition;
import com.formation.centre.model.Locataire;
import com.formation.centre.model.Location;
import com.formation.centre.model.Propriete;
import com.formation.centre.model.Utilisateur;
import com.formation.centre.repository.LocataireRepository;
import com.formation.centre.repository.LocationRepository;
import com.formation.centre.repository.ProprieteRepository;
import com.formation.centre.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UnifiedService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private LocataireRepository locataireRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;


    
    public LocationDTO createLocation(LocationDTO dto) {
        Location location = new Location();
        location.setId(UUID.randomUUID());
        location.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId())).orElseThrow());
        location.setLocataire(locataireRepository.findById(UUID.fromString(dto.getLocataireId())).orElseThrow());
        location.setDateDebut(LocalDate.parse(dto.getDateDebut()));
        location.setDateFin(dto.getDateFin() != null ? LocalDate.parse(dto.getDateFin()) : null);
        location.setLoyerMensuel(new BigDecimal(dto.getLoyerMensuel()));
        location.setChargesMensuelles(new BigDecimal(dto.getChargesMensuelles()));
        location.setDepotGarantie(new BigDecimal(dto.getDepotGarantie()));
        location.setFrequenceLoyer(Location.FrequenceLoyer.valueOf(dto.getFrequenceLoyer()));
        location.setJourEcheance(Integer.parseInt(dto.getJourEcheance()));
        location.setCreeLe(LocalDateTime.now());
        location.setModifieLe(LocalDateTime.now());

        locationRepository.save(location);
        return toDTO(location);
    }

    public LocationDTO updateLocation(String id, LocationDTO dto) {
        Location location = locationRepository.findById(UUID.fromString(id)).orElseThrow();
        location.setPropriete(proprieteRepository.findById(UUID.fromString(dto.getProprieteId())).orElseThrow());
        location.setLocataire(locataireRepository.findById(UUID.fromString(dto.getLocataireId())).orElseThrow());
        location.setDateDebut(LocalDate.parse(dto.getDateDebut()));
        location.setDateFin(dto.getDateFin() != null ? LocalDate.parse(dto.getDateFin()) : null);
        location.setLoyerMensuel(new BigDecimal(dto.getLoyerMensuel()));
        location.setChargesMensuelles(new BigDecimal(dto.getChargesMensuelles()));
        location.setDepotGarantie(new BigDecimal(dto.getDepotGarantie()));
        location.setFrequenceLoyer(Location.FrequenceLoyer.valueOf(dto.getFrequenceLoyer()));
        location.setJourEcheance(Integer.parseInt(dto.getJourEcheance()));
        location.setModifieLe(LocalDateTime.now());

        locationRepository.save(location);
        return toDTO(location);
    }

    public void deleteLocation(String id) {
        locationRepository.deleteById(UUID.fromString(id));
    }

    public List<LocationDTO> getLocationsByUtilisateur(String utilisateurId) {
        return locationRepository.findByPropriete_Utilisateur_Id(UUID.fromString(utilisateurId)).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    

    private LocationDTO toDTO(Location entity) {
        LocationDTO dto = new LocationDTO();
        dto.setId(entity.getId().toString());
        dto.setProprieteId(entity.getPropriete().getId().toString());
        dto.setLocataireId(entity.getLocataire().getId().toString());
        dto.setDateDebut(entity.getDateDebut().toString());
        dto.setDateFin(entity.getDateFin() != null ? entity.getDateFin().toString() : null);
        dto.setLoyerMensuel(entity.getLoyerMensuel().toPlainString());
        dto.setChargesMensuelles(entity.getChargesMensuelles().toPlainString());
        dto.setDepotGarantie(entity.getDepotGarantie().toPlainString());
        dto.setFrequenceLoyer(entity.getFrequenceLoyer().toString());
        dto.setJourEcheance(entity.getJourEcheance().toString());
        return dto;
    }
    public List<ProprieteDTO> getProprietesByUtilisateur(String utilisateurId) {
        UUID userId = UUID.fromString(utilisateurId);
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        return proprieteRepository.findAll().stream()
                .filter(p -> p.getUtilisateur().getId().equals(userId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public ProprieteDTO updatePropriete(String proprieteId, ProprieteDTO dto) {
        Propriete propriete = proprieteRepository.findById(UUID.fromString(proprieteId))
                .orElseThrow(() -> new IllegalArgumentException("Propriété introuvable"));

        propriete.setNom(dto.getNom());
        propriete.setAdresse(dto.getAdresse());
        propriete.setComplementAdresse(dto.getComplementAdresse());
        propriete.setCodePostal(dto.getCodePostal());
        propriete.setVille(dto.getVille());
        propriete.setTypeBien(Propriete.TypeBien.valueOf(dto.getTypeBien()));
        propriete.setDateAcquisition(dto.getDateAcquisition() != null ? LocalDate.parse(dto.getDateAcquisition()) : null);
        propriete.setDateLivraison(dto.getDateLivraison() != null ? LocalDate.parse(dto.getDateLivraison()) : null);
        propriete.setMontantAcquisition(dto.getMontantAcquisition() != null ? new BigDecimal(dto.getMontantAcquisition()) : null);
        propriete.setTantieme(dto.getTantieme() != null ? new BigDecimal(dto.getTantieme()) : null);
        propriete.setFraisNotaire(dto.getFraisNotaire() != null ? new BigDecimal(dto.getFraisNotaire()) : null);
        propriete.setFraisAgence(dto.getFraisAgence() != null ? new BigDecimal(dto.getFraisAgence()) : null);

        // Mise à jour des compositions si présentes dans le DTO
        if (dto.getCompositions() != null) {
            List<CompositionAcquisition> updatedCompositions = dto.getCompositions().stream().map(cdto -> {
                CompositionAcquisition comp;
                if (cdto.getId() != null) {
                    comp = proprieteRepository.findCompositionById(UUID.fromString(cdto.getId()))
                            .orElse(new CompositionAcquisition());
                } else {
                    comp = new CompositionAcquisition();
                    comp.setId(UUID.randomUUID());
                    comp.setPropriete(propriete);
                }
                comp.setCategorie(cdto.getCategorie());
                comp.setMontant(new BigDecimal(cdto.getMontant()));
                comp.setDescription(cdto.getDescription());
                return comp;
            }).collect(Collectors.toList());
            propriete.setCompositions(updatedCompositions);
        }

        return toDto(proprieteRepository.save(propriete));
    }

    public void deletePropriete(String proprieteId) {
        Propriete propriete = proprieteRepository.findById(UUID.fromString(proprieteId))
                .orElseThrow(() -> new IllegalArgumentException("Propriété introuvable"));
        proprieteRepository.delete(propriete);
    }

    public ProprieteDTO createPropriete(String utilisateurId, ProprieteDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(UUID.fromString(utilisateurId))
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        Propriete entity = new Propriete();
        entity.setNom(dto.getNom());
        entity.setAdresse(dto.getAdresse());
        entity.setComplementAdresse(dto.getComplementAdresse());
        entity.setCodePostal(dto.getCodePostal());
        entity.setVille(dto.getVille());

        // Ajout d'un log pour vérifier la valeur reçue
        System.out.println("TypeBien reçu: " + dto.getTypeBien());

        entity.setTypeBien(Propriete.TypeBien.valueOf(dto.getTypeBien()));
        entity.setDateAcquisition(dto.getDateAcquisition() != null ? LocalDate.parse(dto.getDateAcquisition()) : null);
        entity.setDateLivraison(dto.getDateLivraison() != null ? LocalDate.parse(dto.getDateLivraison()) : null);
        entity.setMontantAcquisition(dto.getMontantAcquisition() != null ? new BigDecimal(dto.getMontantAcquisition()) : null);
        entity.setTantieme(dto.getTantieme() != null ? new BigDecimal(dto.getTantieme()) : null);
        entity.setFraisNotaire(dto.getFraisNotaire() != null ? new BigDecimal(dto.getFraisNotaire()) : null);
        entity.setFraisAgence(dto.getFraisAgence() != null ? new BigDecimal(dto.getFraisAgence()) : null);
        entity.setUtilisateur(utilisateur);

        Propriete saved = proprieteRepository.save(entity);
        return toDto(saved);
    }

    public ProprieteDTO addCompositionsToPropriete(String proprieteId, List<CompositionAcquisitionDTO> compositionsDTO) {
        Propriete propriete = proprieteRepository.findById(UUID.fromString(proprieteId))
                .orElseThrow(() -> new IllegalArgumentException("Propriété introuvable"));

        List<CompositionAcquisition> compositions = compositionsDTO.stream().map(cdto -> {
            CompositionAcquisition comp = new CompositionAcquisition();
            comp.setCategorie(cdto.getCategorie());
            comp.setMontant(new BigDecimal(cdto.getMontant()));
            comp.setDescription(cdto.getDescription());
            comp.setPropriete(propriete);
            return comp;
        }).collect(Collectors.toList());

        propriete.setCompositions(compositions);
        Propriete updated = proprieteRepository.save(propriete);
        return toDto(updated);
    }

    public ProprieteDTO createProprieteWithCompositions(String utilisateurId, Object proprieteObj, Object compositionsObj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProprieteDTO proprieteDTO = mapper.convertValue(proprieteObj, ProprieteDTO.class);

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
            if (compositionsObj != null) {
                List<?> compList = (List<?>) compositionsObj;
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
            return toDto(saved);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la propriété et des compositions : " + e.getMessage(), e);
        }
    }


    private ProprieteDTO toDto(Propriete entity) {
        ProprieteDTO dto = new ProprieteDTO();
        dto.setId(entity.getId().toString());
        dto.setNom(entity.getNom());
        dto.setAdresse(entity.getAdresse());
        dto.setComplementAdresse(entity.getComplementAdresse());
        dto.setCodePostal(entity.getCodePostal());
        dto.setVille(entity.getVille());
        dto.setTypeBien(entity.getTypeBien().toString());
        dto.setDateAcquisition(entity.getDateAcquisition() != null ? entity.getDateAcquisition().toString() : null);
        dto.setDateLivraison(entity.getDateLivraison() != null ? entity.getDateLivraison().toString() : null);
        dto.setMontantAcquisition(entity.getMontantAcquisition() != null ? entity.getMontantAcquisition().toPlainString() : null);
        dto.setTantieme(entity.getTantieme() != null ? entity.getTantieme().toPlainString() : null);
        dto.setFraisNotaire(entity.getFraisNotaire() != null ? entity.getFraisNotaire().toPlainString() : null);
        dto.setFraisAgence(entity.getFraisAgence() != null ? entity.getFraisAgence().toPlainString() : null);

        // Ne retourne les compositions que si elles existent
        if (entity.getCompositions() != null) {
            List<CompositionAcquisitionDTO> compositions = entity.getCompositions().stream().map(c -> {
                CompositionAcquisitionDTO cdto = new CompositionAcquisitionDTO();
                cdto.setId(c.getId().toString());
                cdto.setProprieteId(entity.getId().toString());
                cdto.setCategorie(c.getCategorie());
                cdto.setMontant(c.getMontant().toPlainString());
                cdto.setDescription(c.getDescription());
                return cdto;
            }).collect(Collectors.toList());
            dto.setCompositions(compositions);
        }

        return dto;
    }


    public List<LocataireDTO> getLocatairesByUtilisateur(String utilisateurId) {
        return locataireRepository.findByUtilisateur_Id(UUID.fromString(utilisateurId)).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public LocataireDTO creerLocataire(String utilisateurId, LocataireDTO dto) {
        Locataire loc = new Locataire();
        loc.setId(UUID.randomUUID());
        loc.setUtilisateur(utilisateurRepository.findById(UUID.fromString(utilisateurId)).orElseThrow());
        loc.setNom(dto.getNom());
        loc.setTelephone(dto.getTelephone());
        loc.setEmail(dto.getEmail());
        loc.setAdresse(dto.getAdresse());
        loc.setComplementAdresse(dto.getComplementAdresse());
        loc.setCodePostal(dto.getCodePostal());
        loc.setVille(dto.getVille());

        Locataire saved = locataireRepository.save(loc);
        return toDto(saved);
    }

    public LocataireDTO updateLocataire(String locataireId, LocataireDTO dto) {
        Locataire loc = locataireRepository.findById(UUID.fromString(locataireId))
            .orElseThrow(() -> new IllegalArgumentException("Locataire introuvable"));

        loc.setNom(dto.getNom());
        loc.setTelephone(dto.getTelephone());
        loc.setEmail(dto.getEmail());
        loc.setAdresse(dto.getAdresse());
        loc.setComplementAdresse(dto.getComplementAdresse());
        loc.setCodePostal(dto.getCodePostal());
        loc.setVille(dto.getVille());

        Locataire saved = locataireRepository.save(loc);
        return toDto(saved);
    }

    public void deleteLocataire(String locataireId) {
        locataireRepository.deleteById(UUID.fromString(locataireId));
    }

    private LocataireDTO toDto(Locataire loc) {
        LocataireDTO dto = new LocataireDTO();
        dto.setId(loc.getId().toString());
        dto.setUtilisateurId(loc.getUtilisateur().getId().toString());
        dto.setNom(loc.getNom());
        dto.setTelephone(loc.getTelephone());
        dto.setEmail(loc.getEmail());
        dto.setAdresse(loc.getAdresse());
        dto.setComplementAdresse(loc.getComplementAdresse());
        dto.setCodePostal(loc.getCodePostal());
        dto.setVille(loc.getVille());
        return dto;
    }



}
