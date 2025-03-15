package com.formation.centre.dto;

public class UtilisateurDTO {
    private Long id;
    private String email;
    private String motDePasse;

    public UtilisateurDTO() {}

    public UtilisateurDTO(Long id, String email, String motDePasse) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}
