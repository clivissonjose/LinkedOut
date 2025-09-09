package com.va2es.backend.dto;

import com.va2es.backend.models.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoleChangeRequestDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private UserRole newRole;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getNewRole() {
        return newRole;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewRole(UserRole newRole) {
        this.newRole = newRole;
    }
}