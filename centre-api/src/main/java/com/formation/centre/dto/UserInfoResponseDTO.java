package com.formation.centre.dto;

import java.util.List;

public class UserInfoResponseDTO {
    private String userName;
    private String email;
    private List<String> roles;

    public UserInfoResponseDTO(String userName, String email, List<String> roles) {
        this.userName = userName;
        this.email = email;
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
