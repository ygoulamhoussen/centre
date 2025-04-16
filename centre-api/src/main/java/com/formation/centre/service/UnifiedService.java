package com.formation.centre.service;

import com.formation.centre.dto.ProprieteDTO;
import com.formation.centre.dto.CompositionAcquisitionDTO;
import com.formation.centre.model.Propriete;
import com.formation.centre.model.CompositionAcquisition;
import com.formation.centre.model.Utilisateur;
import com.formation.centre.repository.ProprieteRepository;
import com.formation.centre.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnifiedService {

    @Autowired
    private ProprieteRepository proprieteRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<ProprieteDTO> getProprietesByUtilisateur(String utilisateurId) {
        UUID userId = UUID.fromString(utilisateurId);
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        return proprieteRepository.findAll().stream()
                .filter(p -> p.getUtilisateur().getId().equals(userId))
                .map(this::toDto)
                .collect(Collectors.toList());
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

    private ProprieteDTO toDto(Propriete entity) {
        ProprieteDTO dto = new ProprieteDTO();
        dto.setId(entity.getId().toString());
        dto.setNom(entity.getNom());
        dto.setAdresse(entity.getAdresse());
        dto.setComplementAdresse(entity.getComplementAdresse());
        dto.setCodePostal(entity.getCodePostal());
        dto.setVille(entity.getVille());
        dto.setTypeBien(entity.getTypeBien().name());
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
}
