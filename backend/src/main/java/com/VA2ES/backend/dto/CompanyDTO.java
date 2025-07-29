package com.VA2ES.backend.dto;

import com.VA2ES.backend.models.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CompanyDTO {

    @NotBlank(message = "O nome da empresa é obrigatório")
    private String nomeDaEmpresa;

    private String telefone;

    @NotBlank(message = "O CNPJ é obrigatório")
    private String cnpj;

    @NotBlank(message = "A área de atuação é obrigatória")
    private String areaDeAtuacao;

    @NotNull(message = "O representante da empresa é obrigatório")
    private User representanteDaEmpresa;

    public CompanyDTO() {
    }

    public CompanyDTO(String nomeDaEmpresa, String telefone, String cnpj, String areaDeAtuacao, User representanteDaEmpresa) {
        this.nomeDaEmpresa = nomeDaEmpresa;
        this.telefone = telefone;
        this.cnpj = cnpj;
        this.areaDeAtuacao = areaDeAtuacao;
        this.representanteDaEmpresa = representanteDaEmpresa;
    }


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

    public User getRepresentanteDaEmpresa() {
        return representanteDaEmpresa;
    }

    public void setRepresentanteDaEmpresa(User representanteDaEmpresa) {
        this.representanteDaEmpresa = representanteDaEmpresa;
    }
}

