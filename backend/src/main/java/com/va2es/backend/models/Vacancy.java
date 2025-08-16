package com.va2es.backend.models;

import java.time.LocalDate;
import com.va2es.backend.models.enums.VacancyType;
import jakarta.persistence.*;

@Entity
@Table(name = "vacancies")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false, length = 1000)
    private String requisitos;

    @Column(nullable = false)
    private String area;

    @Column(length = 1000)
    private String beneficios;

    @Column()
    private LocalDate dataPublicacao;

    @Column()
    private LocalDate dataLimite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacancyType tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Construtor longo foi removido

    // Método estático para iniciar o Builder
    public static Builder builder() {
        return new Builder();
    }

    // Getters e Setters (continuam os mesmos)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }
    public LocalDate getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDate dataPublicacao) { this.dataPublicacao = dataPublicacao; }
    public LocalDate getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }
    public VacancyType getTipo() { return tipo; }
    public void setTipo(VacancyType tipo) { this.tipo = tipo; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    // Classe estática interna Builder
    public static class Builder {
        private Long id;
        private String titulo;
        private String descricao;
        private String requisitos;
        private String area;
        private String beneficios;
        private LocalDate dataPublicacao;
        private LocalDate dataLimite;
        private VacancyType tipo;
        private Company company;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder titulo(String titulo) { this.titulo = titulo; return this; }
        public Builder descricao(String descricao) { this.descricao = descricao; return this; }
        public Builder requisitos(String requisitos) { this.requisitos = requisitos; return this; }
        public Builder area(String area) { this.area = area; return this; }
        public Builder beneficios(String beneficios) { this.beneficios = beneficios; return this; }
        public Builder dataPublicacao(LocalDate dataPublicacao) { this.dataPublicacao = dataPublicacao; return this; }
        public Builder dataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; return this; }
        public Builder tipo(VacancyType tipo) { this.tipo = tipo; return this; }
        public Builder company(Company company) { this.company = company; return this; }

        public Vacancy build() {
            Vacancy vacancy = new Vacancy();
            vacancy.setId(this.id);
            vacancy.setTitulo(this.titulo);
            vacancy.setDescricao(this.descricao);
            vacancy.setRequisitos(this.requisitos);
            vacancy.setArea(this.area);
            vacancy.setBeneficios(this.beneficios);
            vacancy.setDataPublicacao(this.dataPublicacao);
            vacancy.setDataLimite(this.dataLimite);
            vacancy.setTipo(this.tipo);
            vacancy.setCompany(this.company);
            return vacancy;
        }
    }
}