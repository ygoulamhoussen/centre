package com.formation.centre.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formation.centre.dto.LoginResponseDTO;
import com.formation.centre.dto.UserInfoResponseDTO;
import com.formation.centre.dto.CapitalIdentitesDTO;
import com.formation.centre.dto.RegisterRequestDTO;
import com.formation.centre.model.Utilisateur;
import com.formation.centre.model.CapitalIdentites;
import com.formation.centre.repository.CapitalIdentitesRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;
import java.util.Collections;
import com.formation.centre.model.Role;
import com.formation.centre.repository.RoleRepository;
import com.formation.centre.repository.UtilisateurRepository;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CapitalIdentitesRepository capitalIdentitesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    public LoginResponseDTO login(String userName, String password) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByUserName(userName);

        if (utilisateurOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Comparaison directe (⚠️ mot de passe stocké en clair !)
        if (!password.equals(utilisateur.getMotDePasseHash())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(utilisateur.getUserName());
        String refreshToken = "refresh-" + token; // Factice pour l'instant

        return new LoginResponseDTO(token, refreshToken);
    }

// src/main/java/com/formation/centre/service/AuthService.java
public UserInfoResponseDTO getInfosUtilisateur(String userName) {
    Utilisateur u = utilisateurRepository
        .findByUserName(userName)
        .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    return new UserInfoResponseDTO(
        u.getId().toString(),
        u.getUserName(),
        u.getEmail(),
        u.getRoles().stream().map(r -> r.getName()).toList()
    );
}

    @Transactional
    public void register(RegisterRequestDTO dto) {
        // Vérifier unicité userName/email
        if (utilisateurRepository.findByUserName(dto.userName).isPresent()) {
            throw new RuntimeException("Nom d'utilisateur déjà utilisé");
        }
        // (optionnel) Vérifier unicité email
        // if (utilisateurRepository.findByEmail(dto.email).isPresent()) {
        //     throw new RuntimeException("Email déjà utilisé");
        // }
        Utilisateur user = new Utilisateur();
        user.setId(UUID.randomUUID());
        user.setUserName(dto.userName);
        user.setNom(dto.nom);
        user.setPrenom(dto.prenom);
        user.setEmail(dto.email);
        user.setMotDePasseHash(dto.password); // ⚠️ à remplacer par un hash réel en prod
        user.setCreeLe(LocalDateTime.now());
        user.setModifieLe(LocalDateTime.now());
        // Rôle USER par défaut
        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Rôle USER manquant"));
        user.setRoles(Collections.singletonList(userRole));
        Utilisateur savedUser = utilisateurRepository.save(user);

        // Création capital-identites
        if (dto.capitalIdentites != null) {
            CapitalIdentitesDTO c = dto.capitalIdentites;
            CapitalIdentites entity = new CapitalIdentites();
            entity.setNomExploitant(c.getNomExploitant());
            entity.setPrenomExploitant(c.getPrenomExploitant());
            entity.setAdresseExploitant(c.getAdresseExploitant());
            entity.setCodePostalExploitant(c.getCodePostalExploitant());
            entity.setVilleExploitant(c.getVilleExploitant());
            entity.setQualite(c.getQualite());
            entity.setApportsNumeraires(c.getApportsNumeraires());
            entity.setApportsNature(c.getApportsNature());
            entity.setApportsIndustrie(c.getApportsIndustrie());
            entity.setPartsDetenues(c.getPartsDetenues());
            entity.setUtilisateur(savedUser);
            entity.setDateCreation(LocalDate.now());
            entity.setCreatedAt(LocalDateTime.now());
            entity.calculerTotalCapital();
            capitalIdentitesRepository.save(entity);
        }
    }

}
