package com.VA2ES.backend.dto;

import com.VA2ES.backend.models.enums.UserRole;

public class RegisterDTO {
    private String email;
    private String password;
    private UserRole role;
    private String nome;

    public RegisterDTO() {
    }

    public RegisterDTO(String email, String password, UserRole role, String nome) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
