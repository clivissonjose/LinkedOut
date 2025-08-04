package com.VA2ES.backend.dto;

import java.time.LocalDate;

import com.VA2ES.backend.models.enums.VacancyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VacancyUpdateDTO {
    @NotBlank(message = "O título da vaga é obrigatório.")
    @Size(min = 3, max = 100)
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotBlank(message = "Os requisitos são obrigatórios.")
    private String requisitos;

    @NotBlank(message = "A área de atuação é obrigatória.")
    private String area;

    private String beneficios;

    @NotNull(message = "O tipo da vaga é obrigatório.")
    private VacancyType tipo;

    @NotNull(message = "Data de termino é obrigatório.")
    private LocalDate dataTermino;

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

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

}
