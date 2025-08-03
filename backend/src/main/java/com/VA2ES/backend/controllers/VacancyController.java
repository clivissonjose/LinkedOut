package com.VA2ES.backend.controllers;


import com.VA2ES.backend.dto.VacancyRequestDTO;
import com.VA2ES.backend.dto.VacancyResponseDTO;
import com.VA2ES.backend.dto.VacancyUpdateDTO;
import com.VA2ES.backend.services.VacancyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vagas")
public class VacancyController {
    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VacancyResponseDTO> createVacancy(@Valid @RequestBody VacancyRequestDTO vacancyRequestDTO) {
        return  ResponseEntity.ok(vacancyService.createVacancy(vacancyRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponseDTO> getVacancyById(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.findById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<VacancyResponseDTO>> getAllVacancies() {
        return ResponseEntity.ok(vacancyService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VacancyResponseDTO> updateVacancy(@PathVariable Long id, @Valid @RequestBody VacancyUpdateDTO vacancyUpdateDTO) {
        return ResponseEntity.ok(vacancyService.update(id, vacancyUpdateDTO));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteVacancy(@PathVariable Long id) {
        vacancyService.delete(id);
        return ResponseEntity.ok().build();
    }

}
