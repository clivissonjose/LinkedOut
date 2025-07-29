package com.VA2ES.backend.controllers;

import com.VA2ES.backend.models.Company;
import com.VA2ES.backend.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService empresaService;

    @Autowired
    public CompanyController(CompanyService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Company cadastrar(@RequestBody Company empresa) {
        return empresaService.create(empresa);
    }

    @GetMapping("/list")
    public List<Company> listar() {
        return empresaService.findAll();
    }

    @GetMapping("/search/{id}")
    public Company buscarPorId(@PathVariable Long id) {
        return empresaService.findById(id);
    }

    @PutMapping("/update/{id}")
    public Company atualizar(@PathVariable Long id, @RequestBody Company dadosAtualizados) {
        return empresaService.update(id, dadosAtualizados);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        empresaService.deleteById(id);
    }
}

