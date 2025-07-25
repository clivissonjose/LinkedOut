package com.VA2ES.backend.dto;

import com.VA2ES.backend.models.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RegisterDTO {
    private String email;
    private String password;
    private UserRole role;
    private String nome;
}
