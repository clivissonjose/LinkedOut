package com.VA2ES.backend.dto;

import com.VA2ES.backend.validator.CNPJ;
import com.VA2ES.backend.validator.Phone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompanyRequestDTO {

    @NotBlank(message = "O nome da empresa é obrigatório.")
    public String nomeDaEmpresa;

    @NotNull(message = "O telefone é obrigatório.")
    @Phone
    public String telefone;

    @NotNull(message = "O CNPJ é obrigatório.")
    @CNPJ
    public String cnpj;

    @NotBlank(message = "A área de atuação é obrigatória.")
    public String areaDeAtuacao;

    @NotNull(message = "O ID do representante da empresa é obrigatório.")
    public Long representanteDaEmpresaId;
}

