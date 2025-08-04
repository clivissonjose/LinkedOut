package com.VA2ES.backend.models;

import java.time.LocalDate;
import java.util.Date;

import com.VA2ES.backend.models.enums.VacancyType;
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
    private String requisitos; // Requisitos como texto

    @Column(nullable = false)
    private String area; // Área de atuação

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

    public Vacancy() {
    }

    public Vacancy(Long id, String titulo, String descricao, String requisitos, String area, String beneficios, VacancyType tipo, Company company) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.requisitos = requisitos;
        this.area = area;
        this.beneficios = beneficios;
        this.tipo = tipo;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public VacancyType getTipo() {
        return tipo;
    }

    public void setTipo(VacancyType tipo) {
        this.tipo = tipo;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}


