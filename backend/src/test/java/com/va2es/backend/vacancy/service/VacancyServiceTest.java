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
    private VacancyService vacancyService; // <-- replace with the class that has createVacancy()

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
    void createVacancy_ShouldSaveAndReturnResponse_WhenValidInput() {
        // given
        VacancyRequestDTO request = new VacancyRequestDTO();
        request.setCompanyId(1L);
        request.setTitulo("Desenvolvedor Java");
        request.setDescricao("Desenvolvimento backend");
        request.setRequisitos("Spring Boot, JPA");
        request.setArea("TI");
        request.setBeneficios("Vale refeição");
        request.setTipo(VacancyType.EMPREGO);
        request.setDataTermino(LocalDate.of(2025, 12, 31));

        Company empresa = new Company();
        empresa.setId(1L);
        empresa.setNomeDaEmpresa("Tech Ltda");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(empresa));

        Vacancy vagaSalva = new Vacancy();
        vagaSalva.setId(10L);
        vagaSalva.setTitulo(request.getTitulo());
        vagaSalva.setDescricao(request.getDescricao());
        vagaSalva.setRequisitos(request.getRequisitos());
        vagaSalva.setArea(request.getArea());
        vagaSalva.setBeneficios(request.getBeneficios());
        vagaSalva.setTipo(request.getTipo());
        vagaSalva.setCompany(empresa);
        vagaSalva.setDataPublicacao(LocalDate.now());
        vagaSalva.setDataLimite(request.getDataTermino());

        when(vacancyRepository.save(any(Vacancy.class))).thenReturn(vagaSalva);

        // when
        VacancyResponseDTO response = vacancyService.createVacancy(request);

        // then
        assertNotNull(response);
        assertEquals(10L, response.id);
        assertEquals("Desenvolvedor Java", response.titulo);
        assertEquals("Tech Ltda", response.nomeDaEmpresa);
        assertEquals(VacancyType.EMPREGO, response.tipo);
        assertEquals(LocalDate.now(), response.dataPublicacao);
        assertEquals(LocalDate.of(2025, 12, 31), response.dataTermino);

        verify(vacancyRepository, times(1)).save(any(Vacancy.class));
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
