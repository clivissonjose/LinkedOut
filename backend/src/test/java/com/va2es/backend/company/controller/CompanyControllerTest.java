package com.va2es.backend.company.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
        // Arrange: CompanyRequestDTO sem construtor nem setters -> usar campos públicos
        CompanyRequestDTO request = new CompanyRequestDTO();
        request.nomeDaEmpresa = "Empresa Teste";
        request.cnpj = "12.345.678/0001-90";
        request.telefone = "9876543210";
        request.areaDeAtuacao = "Tecnologia";
        request.representanteDaEmpresaId = 99L;

        
        CompanyResponseDTO mockResponse = mock(CompanyResponseDTO.class);
        when(mockResponse.getId()).thenReturn(1L);
        when(mockResponse.getNomeDaEmpresa()).thenReturn("Empresa Teste");
        when(mockResponse.getCnpj()).thenReturn("12.345.678/0001-90");
        when(mockResponse.getTelefone()).thenReturn("9876543210");
        when(mockResponse.getAreaDeAtuacao()).thenReturn("Tecnologia");
        when(mockResponse.getRepresentanteDaEmpresaId()).thenReturn(99L);

        when(companyService.create(request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<CompanyResponseDTO> response = companyController.cadastrar(request);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Empresa Teste", response.getBody().getNomeDaEmpresa());
        assertEquals("12.345.678/0001-90", response.getBody().getCnpj());
        assertEquals("9876543210", response.getBody().getTelefone());
        assertEquals("Tecnologia", response.getBody().getAreaDeAtuacao());
        assertEquals(99L, response.getBody().getRepresentanteDaEmpresaId());

        verify(companyService, times(1)).create(request);
        verifyNoMoreInteractions(companyService);
    }

    @Test
    void deleteCompanyTest() {
        // Arrange
        Long companyId = 1L;

        // deletar não retorna nada
        doNothing().when(companyService).deleteById(companyId);
        // após deletar, buscar deve lançar EntityNotFoundException
        when(companyService.findById(companyId)).thenThrow(new EntityNotFoundException("Not found"));

        // Act
        companyController.deletar(companyId);

        // Assert: controller deve propagar a exceção ao tentar buscar
        assertThrows(EntityNotFoundException.class, () -> companyController.buscarPorId(companyId));

        verify(companyService, times(1)).deleteById(companyId);
        verify(companyService, times(1)).findById(companyId);
        verifyNoMoreInteractions(companyService);
    }

    @Test
    void testFindById_success() {
        // Arrange
        Long companyId = 1L;
        CompanyResponseDTO mockResponse = mock(CompanyResponseDTO.class);
        when(mockResponse.getId()).thenReturn(companyId);
        when(mockResponse.getNomeDaEmpresa()).thenReturn("Empresa Teste");

        // Supondo que o service lança exceção quando não encontra
        // e retorna o DTO quando encontra:
        when(companyService.findById(companyId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<CompanyResponseDTO> response = companyController.buscarPorId(companyId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(companyId, response.getBody().getId());
        assertEquals("Empresa Teste", response.getBody().getNomeDaEmpresa());

        verify(companyService, times(1)).findById(companyId);
        verifyNoMoreInteractions(companyService);
    }

    @Test
    void testFindById_notFound() {
        // Arrange
        Long companyId = 99L;
        when(companyService.findById(companyId)).thenThrow(new EntityNotFoundException("Not found"));

        // Assert (controller deve propagar a exceção)
        assertThrows(EntityNotFoundException.class, () -> companyController.buscarPorId(companyId));

        verify(companyService, times(1)).findById(companyId);
        verifyNoMoreInteractions(companyService);
    }

    @Test
    void testDeleteCompany_notFound() {
        // Arrange
        Long companyId = 99L;
        doThrow(new EntityNotFoundException("Not found")).when(companyService).deleteById(companyId);

        // Assert (controller deve propagar a exceção)
        assertThrows(EntityNotFoundException.class, () -> companyController.deletar(companyId));

        verify(companyService, times(1)).deleteById(companyId);
        verifyNoMoreInteractions(companyService);
    }

    @Test
    void testUpdateCompany(){
        // Arrange
        Long companyId = 1L;
        CompanyRequestDTO request = new CompanyRequestDTO();
        request.nomeDaEmpresa = "Empresa Atualizada";
        request.cnpj = "98.765.432/0001-10";
        request.telefone = "1234567890";
        request.areaDeAtuacao = "Finanças";
        request.representanteDaEmpresaId = 100L;

        CompanyResponseDTO mockResponse = mock(CompanyResponseDTO.class);
        when(mockResponse.getId()).thenReturn(companyId);
        when(mockResponse.getNomeDaEmpresa()).thenReturn("Empresa Atualizada");
        when(mockResponse.getCnpj()).thenReturn("98.765.432/0001-10");
        when(mockResponse.getTelefone()).thenReturn("1234567890");
        when(mockResponse.getAreaDeAtuacao()).thenReturn("Finanças");
        when(mockResponse.getRepresentanteDaEmpresaId()).thenReturn(100L);

        when(companyService.update(companyId, request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<CompanyResponseDTO> response = companyController.atualizar(companyId, request);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(companyId, response.getBody().getId());
        assertEquals("Empresa Atualizada", response.getBody().getNomeDaEmpresa());
        assertEquals("98.765.432/0001-10", response.getBody().getCnpj());
        assertEquals("1234567890", response.getBody().getTelefone());
        assertEquals("Finanças", response.getBody().getAreaDeAtuacao());
        assertEquals(100L, response.getBody().getRepresentanteDaEmpresaId());

        verify(companyService, times(1)).update(companyId, request);
        verifyNoMoreInteractions(companyService);
    }

    
}
