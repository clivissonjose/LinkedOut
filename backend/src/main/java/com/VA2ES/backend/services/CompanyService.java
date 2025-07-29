package com.VA2ES.backend.services;

import com.VA2ES.backend.models.Company;
import com.VA2ES.backend.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository empresaRepository;

    @Autowired
    public CompanyService(CompanyRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public Company create(Company empresa) {
        return empresaRepository.save(empresa);
    }

    public Company findById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
    }

    public List<Company> findAll() {
        return empresaRepository.findAll();
    }

    public void deleteById(Long id) {
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
        
        empresaRepository.delete(empresa);
    }

    public Company update(Long id, Company dadosAtualizados) {
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        // Verifica se o CNPJ está sendo alterado e se já existe outra empresa com esse CNPJ
        if (!empresa.getCnpj().equals(dadosAtualizados.getCnpj())) {
            empresaRepository.findByCnpj(dadosAtualizados.getCnpj()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ já cadastrado para outra empresa.");
                }
            });
        }
        empresa.setNomeDaEmpresa(dadosAtualizados.getNomeDaEmpresa());
        empresa.setCnpj(dadosAtualizados.getCnpj());
        empresa.setTelefone(dadosAtualizados.getTelefone());
        empresa.setAreaDeAtuacao(dadosAtualizados.getAreaDeAtuacao());
        empresa.setRepresentanteDaEmpresa(dadosAtualizados.getRepresentanteDaEmpresa());

        return empresaRepository.save(empresa);
    }



}
