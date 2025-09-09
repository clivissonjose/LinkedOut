package com.va2es.backend.vacancy.service;

import com.va2es.backend.dto.VacancyRequestDTO;
import com.va2es.backend.dto.VacancyResponseDTO;
import com.va2es.backend.models.Company;
import com.va2es.backend.models.Vacancy;
import com.va2es.backend.models.enums.VacancyType;
import com.va2es.backend.repositories.CompanyRepository;
import com.va2es.backend.repositories.VacancyRepository;
import com.va2es.backend.services.VacancyService;

import jakarta.persistence.EntityNotFoundException;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VacancyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @InjectMocks
    private VacancyService vacancyService; 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vacancyService = spy(vacancyService);
        doNothing().when(vacancyService).checkPermission(anyLong());
    }

    @Test
    void createVacancy_ShouldThrowException_WhenCompanyNotFound() {
        // given
        VacancyRequestDTO request = new VacancyRequestDTO();
        request.setCompanyId(99L);

        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> vacancyService.createVacancy(request));

        assertEquals("Empresa não encontrada", ex.getMessage());
        verify(companyRepository, times(1)).findById(99L);
        verifyNoInteractions(vacancyRepository);
    }


    @Test
    void deleteVacancy_ShouldThrowException_WhenVacancyNotFound() {
        // given
        Long vacancyId = 99L;
        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.empty());

        // when / then
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> vacancyService.delete(vacancyId));

        assertEquals("Vaga não encontrada", ex.getMessage());
        verify(vacancyRepository, times(1)).findById(vacancyId);
        verify(vacancyRepository, never()).delete(any());
    }

    @Test
    void deleteVacancy_ShouldDelete_WhenVacancyExists() {
        // given
        Long vacancyId = 1L;
        Company empresa = new Company();
        empresa.setId(10L);

        Vacancy vaga = new Vacancy();
        vaga.setId(vacancyId);
        vaga.setCompany(empresa);

        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vaga));

        // spy to bypass permission check if necessary
       // vacancyService = spy(vacancyService);
       // doNothing().when(vacancyService).checkPermission(anyLong());

        // when
        vacancyService.delete(vacancyId);

        // then
        verify(vacancyRepository, times(1)).findById(vacancyId);
        verify(vacancyRepository, times(1)).delete(vaga);
    }
}
