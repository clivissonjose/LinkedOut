package com.va2es.backend.controllers;

import com.va2es.backend.dto.CompanyRequestDTO;
import com.va2es.backend.dto.CompanyResponseDTO;
import com.va2es.backend.dto.StudentPublicDTO;
import com.va2es.backend.services.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService empresaService;

    public CompanyController(CompanyService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompanyResponseDTO> cadastrar(@Valid @RequestBody CompanyRequestDTO empresa) {
        return ResponseEntity.ok(empresaService.create(empresa));
    }

    @GetMapping("/list")
    public ResponseEntity<List<CompanyResponseDTO>> listar() {
        return ResponseEntity.ok(empresaService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyResponseDTO>> buscarPorNomeOuArea(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String area) {
        List<CompanyResponseDTO> empresas = empresaService.buscarPorNomeOuArea(nome, area);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<CompanyResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CompanyResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CompanyRequestDTO dadosAtualizados) {
        return ResponseEntity.ok(empresaService.update(id, dadosAtualizados));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        empresaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("students/filter")
    public ResponseEntity<Object> filtroEstudantes(@RequestParam String course, @RequestParam int periodMin, @RequestParam int periodMax) {
        try {
            List<StudentPublicDTO> estudantes = empresaService.filtroEstudantesPorAreaEPeriodo(course, periodMin, periodMax);
            return ResponseEntity.ok(estudantes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}