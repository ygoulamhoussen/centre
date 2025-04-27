// src/main/java/com/formation/centre/dto/UserInfoResponseDTO.java
package com.formation.centre.dto;

import java.util.List;

public class UserInfoResponseDTO {
    private String userId;
    private String userName;
    private String email;
    private List<String> roles;

    public UserInfoResponseDTO() {}

    public UserInfoResponseDTO(String userId, String userName, String email, List<String> roles) {
        this.userId   = userId;
        this.userName = userName;
        this.email    = email;
        this.roles    = roles;
    }

    // ----- Getters & Setters -----
    public String getUserId()       { return userId; }
    public void setUserId(String id){ this.userId = id; }

    public String getUserName()         { return userName; }
    public void setUserName(String u)   { this.userName = u; }

    public String getEmail()            { return email; }
    public void setEmail(String e)      { this.email = e; }

    public List<String> getRoles()      { return roles; }
    public void setRoles(List<String> r){ this.roles = r; }
}
