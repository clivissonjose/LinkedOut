package com.va2es.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CompanyRequestDTO {

    @NotBlank(message = "O nome da empresa é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome da empresa deve ter entre 2 e 100 caracteres.")
    private String nomeDaEmpresa;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "O CNPJ é obrigatório.")
    private String cnpj;

    @NotBlank(message = "A área de atuação é obrigatória.")
    private String areaDeAtuacao;

    @NotNull(message = "O ID do representante da empresa é obrigatório.")
    private Long representanteDaEmpresaId;

    // --- Getters and Setters ---

    public String getNomeDaEmpresa() {
        return nomeDaEmpresa;
    }

    public void setNomeDaEmpresa(String nomeDaEmpresa) {
        this.nomeDaEmpresa = nomeDaEmpresa;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAreaDeAtuacao() {
        return areaDeAtuacao;
    }

    public void setAreaDeAtuacao(String areaDeAtuacao) {
        this.areaDeAtuacao = areaDeAtuacao;
    }

    public Long getRepresentanteDaEmpresaId() {
        return representanteDaEmpresaId;
    }

    public void setRepresentanteDaEmpresaId(Long representanteDaEmpresaId) {
        this.representanteDaEmpresaId = representanteDaEmpresaId;
    }
}