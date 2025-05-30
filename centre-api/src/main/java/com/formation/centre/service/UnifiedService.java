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
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.centre.dto.CompositionAcquisitionDTO;
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
import com.formation.centre.model.CompositionAcquisition;
import com.formation.centre.model.Credit;
import com.formation.centre.model.DocumentEntity;
import com.formation.centre.model.Locataire;
import com.formation.centre.model.Location;
import com.formation.centre.model.Paiement;
import com.formation.centre.model.Propriete;
import com.formation.centre.model.Quittance;
import com.formation.centre.model.Utilisateur;
import com.formation.centre.repository.CreditRepository;
import com.formation.centre.repository.DocumentEntityRepository;
import com.formation.centre.repository.LocataireRepository;
import com.formation.centre.repository.LocationRepository;
import com.formation.centre.repository.PaiementRepository;
import com.formation.centre.repository.ProprieteRepository;
import com.formation.centre.repository.QuittanceRepository;
import com.formation.centre.repository.UtilisateurRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UnifiedService {

    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private ProprieteRepository proprieteRepository;
    @Autowired private LocataireRepository locataireRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private QuittanceRepository quittanceRepository;
    @Autowired private PaiementRepository paiementRepository;
    @Autowired private CreditRepository creditRepository;
    @Autowired private DocumentEntityRepository documentEntityRepository;



    // --- PROPRIETE ---
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
        } else {
            p = new Propriete();
            //p.setId(UUID.randomUUID());
            p.setUtilisateur(user);
            //p.setCreeLe(LocalDateTime.now());
        }
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
        //p.setModifieLe(LocalDateTime.now());

        // compositions
        if (dto.getCompositions() != null && !dto.getCompositions().isEmpty()) {
            List<CompositionAcquisition> comps = dto.getCompositions().stream().map(cdto -> {
                CompositionAcquisition c;
                if (cdto.getId() != null) {
                    c = p.getCompositions().stream()
                         .filter(existing -> existing.getId().toString().equals(cdto.getId()))
                         .findFirst()
                         .orElse(new CompositionAcquisition());
                } else {
                    c = new CompositionAcquisition();
                    c.setPropriete(p);
                }
                c.setCategorie(cdto.getCategorie());
                c.setMontant(new BigDecimal(cdto.getMontant()));
                c.setDescription(cdto.getDescription());
                return c;
            }).collect(Collectors.toList());
            p.setCompositions(comps);
        }

        Propriete saved = proprieteRepository.save(p);
        return toDTO(saved);
    }

    public void deletePropriete(String proprieteId) {
        proprieteRepository.deleteById(UUID.fromString(proprieteId));
    }

    private ProprieteDTO toDTO(Propriete p) {
        ProprieteDTO dto = new ProprieteDTO();
        dto.setId(p.getId().toString());
        dto.setNom(p.getNom());
        dto.setAdresse(p.getAdresse());
        dto.setComplementAdresse(p.getComplementAdresse());
        dto.setCodePostal(p.getCodePostal());
        dto.setVille(p.getVille());
        dto.setTypeBien(p.getTypeBien().toString());
        dto.setDateAcquisition(p.getDateAcquisition() != null ? p.getDateAcquisition().toString() : null);
        dto.setDateLivraison(p.getDateLivraison() != null ? p.getDateLivraison().toString() : null);
        dto.setMontantAcquisition(p.getMontantAcquisition()!=null? p.getMontantAcquisition().toPlainString():null);
        dto.setTantieme(p.getTantieme()!=null? p.getTantieme().toPlainString():null);
        dto.setFraisNotaire(p.getFraisNotaire()!=null? p.getFraisNotaire().toPlainString():null);
        dto.setFraisAgence(p.getFraisAgence()!=null? p.getFraisAgence().toPlainString():null);
        if (p.getCompositions()!=null) {
            dto.setCompositions(p.getCompositions().stream().map(c -> {
                CompositionAcquisitionDTO cd = new CompositionAcquisitionDTO();
                cd.setId(c.getId().toString());
                cd.setCategorie(c.getCategorie());
                cd.setMontant(c.getMontant().toPlainString());
                cd.setDescription(c.getDescription());
                return cd;
            }).collect(Collectors.toList()));
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
        if (dto.getId()!=null && !dto.getId().isEmpty()) {
            q = quittanceRepository.findById(UUID.fromString(dto.getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Quittance introuvable"));
        } else {
            q = new Quittance();
            q.setId(UUID.randomUUID());
            q.setCreeLe(LocalDateTime.now());
        }
        q.setLocation(locationRepository.findById(UUID.fromString(dto.getLocationId())).orElseThrow());
        q.setDateDebut(LocalDate.parse(dto.getDateDebut()));
        q.setDateFin(dto.getDateFin()!=null?LocalDate.parse(dto.getDateFin()):null);
        //q.setDateEmission(LocalDate.parse(dto.getDateEmission()));
        q.setDateEcheance(dto.getDateEcheance()!=null?LocalDate.parse(dto.getDateEcheance()):null);
        q.setMontantLoyer(new BigDecimal(dto.getMontantLoyer()));
        q.setMontantCharges(new BigDecimal(dto.getMontantCharges()));
        //q.setMontantTotal(new BigDecimal(dto.getMontantTotal()));
        q.setStatut(Quittance.StatutQuittance.valueOf(dto.getStatut()));
        q.setInclureCaution(dto.getInclureCaution());
        q.setDepotGarantie(dto.getDepotGarantie()!=null? new BigDecimal(dto.getDepotGarantie()):BigDecimal.ZERO);
        q.setModifieLe(LocalDateTime.now());

        Quittance saved = quittanceRepository.save(q);
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
        return dto;
    }

    // --- PAIEMENT ---
    public List<PaiementDTO> getPaiementsByUtilisateur(String utilisateurId) {
        UUID uid = UUID.fromString(utilisateurId);
        return paiementRepository.findByQuittance_Location_Propriete_Utilisateur_Id(uid)
                .stream()
                .map(this::toDto)
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
        return toDto(saved);
    }

    public void deletePaiement(String id) {
        paiementRepository.deleteById(UUID.fromString(id));
    }

    private PaiementDTO toDto(Paiement p) {
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

    public void deleteCredit(String id) {
        creditRepository.deleteById(UUID.fromString(id));
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
    
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
    
            document.add(new Paragraph("Quittance de loyer"));
            document.add(new Paragraph("Date d'émission : " + quittance.getDateEmission()));
            document.add(new Paragraph("Période : " + quittance.getDateDebut() + " au " + quittance.getDateFin()));
            document.add(new Paragraph("Montant total : " + quittance.getMontantTotal() + " €"));
            document.add(new Paragraph("Statut : " + quittance.getStatut()));
    
            document.close();
    
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur de génération PDF : " + e.getMessage(), e);
        }
    }

    public DocumentDTO saveDocument(String fichier, String utilisateurId, String proprieteId, String locataireId, String typeDocument, String titre, String dateDocument) {

        DocumentEntity doc = new DocumentEntity();
        doc.setId(UUID.randomUUID());
        doc.setUtilisateur(utilisateurRepository.findById(UUID.fromString(utilisateurId)).orElseThrow());
        if (proprieteId != null) {
            doc.setPropriete(proprieteRepository.findById(UUID.fromString(proprieteId)).orElse(null));
        }
        if (locataireId != null) {
            doc.setLocataire(locataireRepository.findById(UUID.fromString(locataireId)).orElse(null));
        }
        doc.setTypeDocument(DocumentEntity.TypeDocument.valueOf(typeDocument));
        doc.setTitre(titre);
        doc.setDateDocument(LocalDate.parse(dateDocument));
        doc.setContenu(fichier);
        doc.setCreeLe(LocalDateTime.now());
        doc.setModifieLe(LocalDateTime.now());

        documentEntityRepository.save(doc);
        return toDTO(doc);

}


private DocumentDTO toDTO(DocumentEntity document) {
    DocumentDTO dto = new DocumentDTO();
    dto.setId(document.getId().toString());
    dto.setTitre(document.getTitre());
    dto.setTypeDocument(document.getTypeDocument().name());
    dto.setDateDocument(document.getDateDocument() != null ? document.getDateDocument().toString() : null);

    if (document.getUtilisateur() != null) {
        dto.setUtilisateurId(document.getUtilisateur().getId().toString());
    }
    if (document.getPropriete() != null) {
        dto.setProprieteId(document.getPropriete().getId().toString());
    }
    if (document.getLocataire() != null) {
        dto.setLocataireId(document.getLocataire().getId().toString());
    }

    // Le contenu est optionnel dans le DTO, on le met uniquement si nécessaire
    //encoder le contenu de document en base64  
    //String encodedContent = Base64.getEncoder().encodeToString(document.getContenu());

     dto.setContenu(document.getContenu());

    return dto;
}



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
    
public CompositionAcquisitionDTO toDTO(CompositionAcquisition c) {
    CompositionAcquisitionDTO dto = new CompositionAcquisitionDTO();
    dto.setId(c.getId().toString());
    dto.setCategorie(c.getCategorie());
    dto.setMontant(c.getMontant().toPlainString());
    dto.setDescription(c.getDescription());
    return dto;
}



public ProprieteDetailDTO getProprieteDetail(UUID proprieteId) {
    Propriete propriete = proprieteRepository.findById(proprieteId)
        .orElseThrow(() -> new RuntimeException("Propriété non trouvée"));

    List<CompositionAcquisitionDTO> compositions = propriete.getCompositions().stream()
        .map(this::toDTO)
        .toList();

    List<DocumentDTO> documents = documentEntityRepository.findByPropriete_Id(proprieteId).stream()
        .map(this::toDTO)
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
        .filter(q -> "IMPAYEE".equals(q.getStatut()) || "PARTIELLE".equals(q.getStatut()))
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
            TreeMap::new, // pour les trier dans l’ordre chronologique
            Collectors.reducing(BigDecimal.ZERO, Paiement::getMontant, BigDecimal::add)
        ));

    DashboardDTO dto = new DashboardDTO();
    dto.setNombreBiens(totalBiens);
    dto.setRepartitionParType(repartitionParType);
    dto.setTauxOccupation(tauxOccupation);
    dto.setNombreImpayes(impayes.size());
    dto.setTotalLoyersPerçus(totalLoyersPerçus);
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




    }





