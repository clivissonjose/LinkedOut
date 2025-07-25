package com.VA2ES.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameDTO(
        @NotBlank(message = "O novo nome não pode estar em branco")
        String newName
) {}