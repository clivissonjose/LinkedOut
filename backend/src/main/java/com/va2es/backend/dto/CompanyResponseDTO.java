package com.va2es.backend.dto;

public class CompanyResponseDTO {
    public Long id;
    public String nomeDaEmpresa;
    public String telefone;
    public String cnpj;
    public String areaDeAtuacao;
    public Long representanteDaEmpresaId;
    public String representanteDaEmpresaNome;

    public CompanyResponseDTO(Long id, String nomeDaEmpresa, String telefone, String cnpj, String areaDeAtuacao, Long representanteDaEmpresaId, String representanteDaEmpresaNome) {
        this.id = id;
        this.nomeDaEmpresa = nomeDaEmpresa;
        this.telefone = telefone;
        this.cnpj = cnpj;
        this.areaDeAtuacao = areaDeAtuacao;
        this.representanteDaEmpresaId = representanteDaEmpresaId;
        this.representanteDaEmpresaNome = representanteDaEmpresaNome;
    }

}
