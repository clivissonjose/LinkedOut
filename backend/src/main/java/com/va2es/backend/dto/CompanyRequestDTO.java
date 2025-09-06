package com.va2es.backend.dto;

import com.va2es.backend.validator.CNPJ;
import com.va2es.backend.validator.Phone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompanyRequestDTO {

    @NotBlank(message = "O nome da empresa é obrigatório.")
    public String nomeDaEmpresa;

    @NotNull(message = "O telefone é obrigatório.")
    public String telefone;

    @NotNull(message = "O CNPJ é obrigatório.")
    public String cnpj;

    @NotBlank(message = "A área de atuação é obrigatória.")
    public String areaDeAtuacao;

    @NotNull(message = "O ID do representante da empresa é obrigatório.")
    public Long representanteDaEmpresaId;
}

