package com.va2es.backend.models.enums;

public enum UserRole {
    USER("USER"),
    STUDENT("STUDENT"),
    GESTOR("GESTOR"),
    ADMIN("ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}