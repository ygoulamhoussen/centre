package com.formation.centre.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formation.centre.dto.LoginResponseDTO;
import com.formation.centre.dto.UserInfoResponseDTO;
import com.formation.centre.model.Utilisateur;
import com.formation.centre.repository.UtilisateurRepository;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

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

}
