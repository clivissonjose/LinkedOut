package com.va2es.backend.dto;

public class RegisterDTO {
    private String email;
    private String password;
    private String nome;

    public RegisterDTO() {
    }

    public RegisterDTO(String email, String password, String nome) {
        this.email = email;
        this.password = password;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}