package com.VA2ES.backend.dto;

import com.VA2ES.backend.models.enums.UserRole;


public class RegisterDTO {

    private String email;
    private String nome;
    private String password;
    private UserRole role;

    public RegisterDTO() {
    }

    public RegisterDTO(String email, String nome, String password, UserRole role) {
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
