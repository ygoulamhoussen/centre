package com.formation.centre.service;

import java.util.List;
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

    public UserInfoResponseDTO getInfosUtilisateur(String userName) {
        Utilisateur utilisateur = utilisateurRepository.findByUserName(userName)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return new UserInfoResponseDTO(
            utilisateur.getUserName(),
            utilisateur.getEmail(),
            List.of("admin") // à adapter si tu ajoutes les rôles plus tard
        );
    }
}
