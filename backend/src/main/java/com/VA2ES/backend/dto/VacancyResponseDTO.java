package com.VA2ES.backend.dto;

import com.VA2ES.backend.models.enums.VacancyType;

public class VacancyResponseDTO {
    public Long id;
    public String titulo;
    public  String descricao;
    public String requisitos;
    public String areaDeAtuacao;
    public String beneficios;
    public VacancyType tipo;
    public Long idDaEmpresa;
    public String nomeDaEmpresa;

    public VacancyResponseDTO(Long id, String titulo, String descricao, String requisitos, String areaDeAtuacao, String beneficios, VacancyType tipo, Long idDaEmpresa, String nomeDaEmpresa) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.requisitos = requisitos;
        this.areaDeAtuacao = areaDeAtuacao;
        this.beneficios = beneficios;
        this.tipo = tipo;
        this.idDaEmpresa = idDaEmpresa;
        this.nomeDaEmpresa = nomeDaEmpresa;
    }
}
