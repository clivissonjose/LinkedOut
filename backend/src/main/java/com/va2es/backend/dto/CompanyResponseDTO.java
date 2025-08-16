package com.va2es.backend.dto;

public class CompanyResponseDTO {
    private final Long id;
    private final String nomeDaEmpresa;
    private final String telefone;
    private final String cnpj;
    private final String areaDeAtuacao;
    private final Long representanteDaEmpresaId;
    private final String representanteDaEmpresaNome;

    public CompanyResponseDTO(Long id, String nomeDaEmpresa, String telefone, String cnpj, String areaDeAtuacao, Long representanteDaEmpresaId, String representanteDaEmpresaNome) {
        this.id = id;
        this.nomeDaEmpresa = nomeDaEmpresa;
        this.telefone = telefone;
        this.cnpj = cnpj;
        this.areaDeAtuacao = areaDeAtuacao;
        this.representanteDaEmpresaId = representanteDaEmpresaId;
        this.representanteDaEmpresaNome = representanteDaEmpresaNome;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNomeDaEmpresa() {
        return nomeDaEmpresa;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getAreaDeAtuacao() {
        return areaDeAtuacao;
    }

    public Long getRepresentanteDaEmpresaId() {
        return representanteDaEmpresaId;
    }

    public String getRepresentanteDaEmpresaNome() {
        return representanteDaEmpresaNome;
    }
}