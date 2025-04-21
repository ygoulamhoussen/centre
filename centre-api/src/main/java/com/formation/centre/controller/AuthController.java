package com.formation.centre.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.centre.dto.ApiResponse;
import com.formation.centre.dto.LoginResponseDTO;
import com.formation.centre.dto.UserInfoResponseDTO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody Map<String, String> donnees) {
        String email = donnees.get("userName");
        String motDePasse = donnees.get("password");
    
        if (!email.equals("Soybean") || !motDePasse.equals("123456")) {
            return ResponseEntity.status(401).build();
        }
    
        LoginResponseDTO dto = new LoginResponseDTO("jeton-test-1234", "refresh-jeton-456");
    
        ApiResponse<LoginResponseDTO> response = new ApiResponse<>("0000", "Connexion réussie", dto);
    
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<ApiResponse<UserInfoResponseDTO>> getInfosUtilisateur() {
        UserInfoResponseDTO userInfo = new UserInfoResponseDTO(
            "Jean Dupont",
            "demo@email.fr",
            List.of("admin")
        );
    
        ApiResponse<UserInfoResponseDTO> response = new ApiResponse<>(
            "0000",
            "Succès",
            userInfo
        );
    
        return ResponseEntity.ok(response);
    }
}
