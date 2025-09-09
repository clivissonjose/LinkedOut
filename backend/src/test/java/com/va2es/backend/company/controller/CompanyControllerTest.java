package com.va2es.backend.company.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.va2es.backend.controllers.CompanyController;
import com.va2es.backend.dto.CompanyRequestDTO;
import com.va2es.backend.dto.CompanyResponseDTO;
import com.va2es.backend.services.CompanyService;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @Test
    void testCreateCompany() {
        // Arrange: Usando setters para configurar o DTO
        CompanyRequestDTO request = new CompanyRequestDTO();
        request.setNomeDaEmpresa("Empresa Teste");
        request.setCnpj("12.345.678/0001-90");
        request.setTelefone("9876543210");
        request.setAreaDeAtuacao("Tecnologia");
        request.setRepresentanteDaEmpresaId(99L);

        // Usando o Builder do DTO de resposta para criar um mock de retorno
        CompanyResponseDTO mockResponse = CompanyResponseDTO.builder()
                .id(1L)
                .nomeDaEmpresa("Empresa Teste")
                .cnpj("12.345.678/0001-90")
                .telefone("9876543210")
                .areaDeAtuacao("Tecnologia")
                .idDoRepresentante(99L)
                .build();

        when(companyService.create(any(CompanyRequestDTO.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<CompanyResponseDTO> response = companyController.cadastrar(request);

        // Assert: Usando getters para verificar os resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Empresa Teste", response.getBody().getNomeDaEmpresa());
        assertEquals(99L, response.getBody().getIdDoRepresentante()); // Corrigido para o getter correto

        verify(companyService, times(1)).create(any(CompanyRequestDTO.class));
    }

    @Test
    void deleteCompanyTest() {
        // Arrange
        Long companyId = 1L;
        doNothing().when(companyService).deleteById(companyId);

        // Act
        ResponseEntity<Void> response = companyController.deletar(companyId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(companyService, times(1)).deleteById(companyId);
    }

    @Test
    void testFindById_success() {
        // Arrange
        Long companyId = 1L;
        CompanyResponseDTO mockResponse = CompanyResponseDTO.builder().id(companyId).nomeDaEmpresa("Empresa Teste").build();

        when(companyService.findById(companyId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<CompanyResponseDTO> response = companyController.buscarPorId(companyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(companyId, response.getBody().getId());
        assertEquals("Empresa Teste", response.getBody().getNomeDaEmpresa());

        verify(companyService, times(1)).findById(companyId);
    }

    @Test
    void testFindById_notFound() {
        // Arrange
        Long companyId = 99L;
        when(companyService.findById(companyId)).thenThrow(new EntityNotFoundException("Not found"));

        // Assert
        assertThrows(EntityNotFoundException.class, () -> companyController.buscarPorId(companyId));

        verify(companyService, times(1)).findById(companyId);
    }

    @Test
    void testUpdateCompany() {
        // Arrange
        Long companyId = 1L;
        CompanyRequestDTO request = new CompanyRequestDTO();
        request.setNomeDaEmpresa("Empresa Atualizada");
        request.setRepresentanteDaEmpresaId(100L);

        CompanyResponseDTO mockResponse = CompanyResponseDTO.builder()
                .id(companyId)
                .nomeDaEmpresa("Empresa Atualizada")
                .idDoRepresentante(100L)
                .build();

        when(companyService.update(eq(companyId), any(CompanyRequestDTO.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<CompanyResponseDTO> response = companyController.atualizar(companyId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(companyId, response.getBody().getId());
        assertEquals("Empresa Atualizada", response.getBody().getNomeDaEmpresa());
        assertEquals(100L, response.getBody().getIdDoRepresentante()); // Corrigido para o getter correto

        verify(companyService, times(1)).update(eq(companyId), any(CompanyRequestDTO.class));
    }
}

