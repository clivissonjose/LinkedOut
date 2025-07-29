package com.VA2ES.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeDaEmpresa;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = true)
    private String telefone;

    @Column(nullable = false)
    private String areaDeAtuacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // Muitos empresas para 1 usuário
    @JoinColumn(name = "representante_id", nullable = false)  // nome da coluna FK na tabela empresa
    private User representanteDaEmpresa;
    
    public Company() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeDaEmpresa() { return nomeDaEmpresa; }
    public void setNomeDaEmpresa(String nomeDaEmpresa) { this.nomeDaEmpresa = nomeDaEmpresa; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getAreaDeAtuacao() { return areaDeAtuacao; }
    public void setAreaDeAtuacao(String areaDeAtuacao) { this.areaDeAtuacao = areaDeAtuacao; }

    public User getRepresentanteDaEmpresa() { return representanteDaEmpresa; }
    public void setRepresentanteDaEmpresa(User representanteDaEmpresa) { this.representanteDaEmpresa = representanteDaEmpresa; }
    
    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nomeDaEmpresa='" + nomeDaEmpresa + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", telefone='" + telefone + '\'' +
                ", areaDeAtuacao='" + areaDeAtuacao + '\'' +
                ", representanteDaEmpresa=" + (representanteDaEmpresa != null ? representanteDaEmpresa.getId() : "null") +
                '}';
    }

}
