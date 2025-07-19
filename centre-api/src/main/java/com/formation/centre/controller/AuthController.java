package com.formation.centre.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.centre.dto.ApiResponse;
import com.formation.centre.dto.LoginResponseDTO;
import com.formation.centre.dto.UserInfoResponseDTO;
import com.formation.centre.service.AuthService;
import com.formation.centre.service.JwtService;
import com.formation.centre.service.UnifiedService;
import com.formation.centre.dto.CapitalIdentitesDTO;
import com.formation.centre.dto.RegisterRequestDTO;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UnifiedService unifiedService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody Map<String, String> donnees) {
        try {
            String userName = donnees.get("userName");
            String motDePasse = donnees.get("password");

            LoginResponseDTO dto = authService.login(userName, motDePasse);
            ApiResponse<LoginResponseDTO> response = new ApiResponse<>("0000", "Connexion réussie", dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse<>("401", e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {
        try {
            authService.register(dto);
            return ResponseEntity.ok().body("Inscription réussie");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/start-registration")
    public ResponseEntity<?> startRegistration(@RequestBody RegisterRequestDTO dto) {
        try {
            authService.startRegistration(dto);
            return ResponseEntity.ok().body("Code envoyé à l'email");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/confirm-registration")
    public ResponseEntity<?> confirmRegistration(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        try {
            authService.confirmRegistration(email, code);
            return ResponseEntity.ok().body("Inscription validée, compte créé");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/send-reset-code")
    public ResponseEntity<?> sendResetCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            authService.sendResetCode(email);
            return ResponseEntity.ok().body("Code envoyé");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'envoi du code : " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        String newPassword = body.get("password");
        if (!authService.verifyResetCode(email, code)) {
            return ResponseEntity.status(400).body("Code de vérification invalide");
        }
        try {
            authService.changePasswordByEmail(email, newPassword);
            authService.clearResetCode(email);
            return ResponseEntity.ok().body("Mot de passe réinitialisé");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la réinitialisation : " + e.getMessage());
        }
    }

    @PostMapping("/getEmailByUserName")
    public ResponseEntity<?> getEmailByUserName(@RequestBody Map<String, String> body) {
        String userName = body.get("userName");
        var userOpt = authService.getUserByUserName(userName);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé"));
        }
        String email = userOpt.get().getEmail();
        return ResponseEntity.ok(Map.of("email", email));
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<ApiResponse<UserInfoResponseDTO>> getInfosUtilisateur(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replaceFirst("^Bearer ", "");
        String userName = jwtService.extraireUserNameDepuisToken(token);
        UserInfoResponseDTO dto = authService.getInfosUtilisateur(userName);
        return ResponseEntity.ok(new ApiResponse<>("0000", "Succès", dto));
    }
}
