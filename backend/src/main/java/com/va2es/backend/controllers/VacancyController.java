package com.va2es.backend.controllers;

import com.va2es.backend.dto.VacancyRequestDTO;
import com.va2es.backend.dto.VacancyResponseDTO;
import com.va2es.backend.dto.VacancyUpdateDTO;
import com.va2es.backend.models.enums.VacancyType;
import com.va2es.backend.services.VacancyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vagas")
public class VacancyController {

    private static final Logger logger = LoggerFactory.getLogger(VacancyController.class);
    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VacancyResponseDTO> createVacancy(@Valid @RequestBody VacancyRequestDTO vacancyRequestDTO) {
        return ResponseEntity.ok(vacancyService.createVacancy(vacancyRequestDTO));
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
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        vacancyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<VacancyResponseDTO>> filterVacancies(@RequestParam String area, @RequestParam VacancyType vacancyType,
                                                                    @RequestParam(required = false) LocalDate inicio, @RequestParam(required = false) LocalDate fim) {

        String sanitizedArea = area.replaceAll("[\r\n]", "_");

        logger.info("Filtrando vagas com área: {}, tipo: {}, início: {}, fim: {}", sanitizedArea, vacancyType, inicio, fim);

        if (inicio != null && fim != null) {
            return ResponseEntity.ok(vacancyService.filterByAreaAndTipoAndPeriodo(area, vacancyType, inicio, fim));
        }

        return ResponseEntity.ok(vacancyService.filterByAreaAndTipo(area, vacancyType));
    }
}