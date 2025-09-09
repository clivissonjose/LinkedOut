package com.va2es.backend.dto;

import java.time.LocalDate;
import com.va2es.backend.models.enums.VacancyType;

public class VacancyResponseDTO {
    private final Long id;
    private final String titulo;
    private final String descricao;
    private final String requisitos;
    private final String areaDeAtuacao;
    private final String beneficios;
    private final VacancyType tipo;
    private final Long idDaEmpresa;
    private final String nomeDaEmpresa;
    private final LocalDate dataPublicacao;
    private final LocalDate dataTermino;

    // Construtor é privado e chamado apenas pelo Builder
    private VacancyResponseDTO(Builder builder) {
        this.id = builder.id;
        this.titulo = builder.titulo;
        this.descricao = builder.descricao;
        this.requisitos = builder.requisitos;
        this.areaDeAtuacao = builder.areaDeAtuacao;
        this.beneficios = builder.beneficios;
        this.tipo = builder.tipo;
        this.idDaEmpresa = builder.idDaEmpresa;
        this.nomeDaEmpresa = builder.nomeDaEmpresa;
        this.dataPublicacao = builder.dataPublicacao;
        this.dataTermino = builder.dataTermino;
    }

    // Apenas Getters são públicos
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getRequisitos() { return requisitos; }
    public String getAreaDeAtuacao() { return areaDeAtuacao; }
    public String getBeneficios() { return beneficios; }
    public VacancyType getTipo() { return tipo; }
    public Long getIdDaEmpresa() { return idDaEmpresa; }
    public String getNomeDaEmpresa() { return nomeDaEmpresa; }
    public LocalDate getDataPublicacao() { return dataPublicacao; }
    public LocalDate getDataTermino() { return dataTermino; }

    // Método estático para iniciar o processo de construção
    public static Builder builder() {
        return new Builder();
    }

    // Classe interna estática Builder
    public static final class Builder {
        private Long id;
        private String titulo;
        private String descricao;
        private String requisitos;
        private String areaDeAtuacao;
        private String beneficios;
        private VacancyType tipo;
        private Long idDaEmpresa;
        private String nomeDaEmpresa;
        private LocalDate dataPublicacao;
        private LocalDate dataTermino;

        private Builder() {}

        public Builder id(Long id) { this.id = id; return this; }
        public Builder titulo(String titulo) { this.titulo = titulo; return this; }
        public Builder descricao(String descricao) { this.descricao = descricao; return this; }
        public Builder requisitos(String requisitos) { this.requisitos = requisitos; return this; }
        public Builder areaDeAtuacao(String areaDeAtuacao) { this.areaDeAtuacao = areaDeAtuacao; return this; }
        public Builder beneficios(String beneficios) { this.beneficios = beneficios; return this; }
        public Builder tipo(VacancyType tipo) { this.tipo = tipo; return this; }
        public Builder idDaEmpresa(Long idDaEmpresa) { this.idDaEmpresa = idDaEmpresa; return this; }
        public Builder nomeDaEmpresa(String nomeDaEmpresa) { this.nomeDaEmpresa = nomeDaEmpresa; return this; }
        public Builder dataPublicacao(LocalDate dataPublicacao) { this.dataPublicacao = dataPublicacao; return this; }
        public Builder dataTermino(LocalDate dataTermino) { this.dataTermino = dataTermino; return this; }

        public VacancyResponseDTO build() {
            return new VacancyResponseDTO(this);
        }
    }
}