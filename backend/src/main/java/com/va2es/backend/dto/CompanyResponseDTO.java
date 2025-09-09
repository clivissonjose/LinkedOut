package com.va2es.backend.dto;

public class CompanyResponseDTO {
    private final Long id;
    private final String nomeDaEmpresa;
    private final String telefone;
    private final String cnpj;
    private final String areaDeAtuacao;
    private final Long idDoRepresentante;
    private final String nomeDoRepresentante;

    // Construtor privado que usa o Builder
    private CompanyResponseDTO(Builder builder) {
        this.id = builder.id;
        this.nomeDaEmpresa = builder.nomeDaEmpresa;
        this.telefone = builder.telefone;
        this.cnpj = builder.cnpj;
        this.areaDeAtuacao = builder.areaDeAtuacao;
        this.idDoRepresentante = builder.idDoRepresentante;
        this.nomeDoRepresentante = builder.nomeDoRepresentante;
    }

    // Apenas Getters
    public Long getId() { return id; }
    public String getNomeDaEmpresa() { return nomeDaEmpresa; }
    public String getTelefone() { return telefone; }
    public String getCnpj() { return cnpj; }
    public String getAreaDeAtuacao() { return areaDeAtuacao; }
    public Long getIdDoRepresentante() { return idDoRepresentante; }
    public String getNomeDoRepresentante() { return nomeDoRepresentante; }

    // Método estático para iniciar o Builder
    public static Builder builder() {
        return new Builder();
    }

    // Classe Builder
    public static final class Builder {
        private Long id;
        private String nomeDaEmpresa;
        private String telefone;
        private String cnpj;
        private String areaDeAtuacao;
        private Long idDoRepresentante;
        private String nomeDoRepresentante;

        private Builder() {}

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nomeDaEmpresa(String nomeDaEmpresa) { this.nomeDaEmpresa = nomeDaEmpresa; return this; }
        public Builder telefone(String telefone) { this.telefone = telefone; return this; }
        public Builder cnpj(String cnpj) { this.cnpj = cnpj; return this; }
        public Builder areaDeAtuacao(String areaDeAtuacao) { this.areaDeAtuacao = areaDeAtuacao; return this; }
        public Builder idDoRepresentante(Long idDoRepresentante) { this.idDoRepresentante = idDoRepresentante; return this; }
        public Builder nomeDoRepresentante(String nomeDoRepresentante) { this.nomeDoRepresentante = nomeDoRepresentante; return this; }

        public CompanyResponseDTO build() {
            return new CompanyResponseDTO(this);
        }
    }
}