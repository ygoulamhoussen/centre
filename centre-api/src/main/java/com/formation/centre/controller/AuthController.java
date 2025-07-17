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

    @GetMapping("/getUserInfo")
    public ResponseEntity<ApiResponse<UserInfoResponseDTO>> getInfosUtilisateur(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replaceFirst("^Bearer ", "");
        String userName = jwtService.extraireUserNameDepuisToken(token);
        UserInfoResponseDTO dto = authService.getInfosUtilisateur(userName);
        return ResponseEntity.ok(new ApiResponse<>("0000", "Succès", dto));
    }
}
