package com.va2es.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.va2es.backend.models.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByNomeDaEmpresaIgnoreCase(String nomeDaEmpresa);
    Optional<Company> findByCnpj(String cnpj);
    Optional<Company> findById(Long id);
    List<Company> findByRepresentanteDaEmpresa_Id(Long representanteId);
    List<Company> findByNomeDaEmpresaContainingIgnoreCaseOrAreaDeAtuacaoContainingIgnoreCase(String nome, String area);
}
