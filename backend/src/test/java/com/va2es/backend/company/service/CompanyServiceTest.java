package com.va2es.backend.company.service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import com.va2es.backend.dto.CompanyRequestDTO;
import com.va2es.backend.dto.CompanyResponseDTO;
import com.va2es.backend.models.User;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.UserRepository;
import com.va2es.backend.services.CompanyService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // usa H2 em memória
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) 
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserRepository userRepository;

    //  Método auxiliar para criar e autenticar usuário
    private User createAndAuthenticateUser(String email) {
        User user = new User(
                email,
                "password123",
                UserRole.ADMIN,
                "João de Teste"
        );
        User savedUser = userRepository.save(user);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(savedUser, null, savedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return savedUser;
    }

    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void createCompanySuccessfully() {
        User savedUser = createAndAuthenticateUser("user@test.com");

        CompanyRequestDTO dto = new CompanyRequestDTO();
        dto.nomeDaEmpresa = "Lojas Petronio";
        dto.telefone = "8199800432";
        dto.cnpj = "49.789.000/0091-65";
        dto.areaDeAtuacao = "enxovado";
        dto.representanteDaEmpresaId = savedUser.getId();

        CompanyResponseDTO savedCompany = companyService.create(dto);

        assertNotNull(savedCompany.getId());
        assertEquals("Lojas Petronio", savedCompany.getNomeDaEmpresa());
        assertEquals("8199800432", savedCompany.getTelefone());
        assertEquals("49.789.000/0091-65", savedCompany.getCnpj());
        assertEquals("enxovado", savedCompany.getAreaDeAtuacao());
        assertEquals(savedUser.getId(), savedCompany.getRepresentanteDaEmpresaId());
    }

    @Test
    public void deleteCompanySuccessfully() {
        User savedUser = createAndAuthenticateUser("user@test1.com");

        CompanyRequestDTO dto = new CompanyRequestDTO();
        dto.nomeDaEmpresa = "Lojas Petronio";
        dto.telefone = "8199800433";
        dto.cnpj = "49.789.000/0091-68";
        dto.areaDeAtuacao = "enxovado";
        dto.representanteDaEmpresaId = savedUser.getId();

        CompanyResponseDTO savedCompany = companyService.create(dto);

        assertNotNull(savedCompany);

        companyService.deleteById(savedCompany.getId());

        assertThrows(EntityNotFoundException.class, () -> companyService.findById(savedCompany.getId()));
    }

    @Test
    public void updateCompanySuccessfully() {
        User savedUser = createAndAuthenticateUser("user@test2.com");

        CompanyRequestDTO dto = new CompanyRequestDTO();
        dto.nomeDaEmpresa = "Lojas Petronio";
        dto.telefone = "8199800444";
        dto.cnpj = "49.789.000/0091-99";
        dto.areaDeAtuacao = "enxovado";
        dto.representanteDaEmpresaId = savedUser.getId();

        CompanyResponseDTO savedCompany = companyService.create(dto);

        assertNotNull(savedCompany);

        dto.nomeDaEmpresa = "Lojas Updated";
        dto.telefone = "00000000000";

        CompanyResponseDTO updatedCompany = companyService.update(savedCompany.getId(), dto);

        assertEquals("Lojas Updated", updatedCompany.getNomeDaEmpresa());
        assertEquals("00000000000", updatedCompany.getTelefone());
      
    }
   
    }
