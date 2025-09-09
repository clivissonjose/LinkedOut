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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserRepository userRepository;

    // Helper method to create and authenticate a user
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
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createCompanySuccessfully() {
        User savedUser = createAndAuthenticateUser("user@test.com");

        // Use setters to build the DTO
        CompanyRequestDTO dto = new CompanyRequestDTO();
        dto.setNomeDaEmpresa("Lojas Petronio");
        dto.setTelefone("8199800432");
        dto.setCnpj("49.789.000/0091-65");
        dto.setAreaDeAtuacao("enxovado");
        dto.setRepresentanteDaEmpresaId(savedUser.getId());

        CompanyResponseDTO savedCompany = companyService.create(dto);

        assertNotNull(savedCompany.getId());
        assertEquals("Lojas Petronio", savedCompany.getNomeDaEmpresa());
        assertEquals("8199800432", savedCompany.getTelefone());
        assertEquals("49.789.000/0091-65", savedCompany.getCnpj());
        assertEquals("enxovado", savedCompany.getAreaDeAtuacao());
        // Corrected to use the existing getter from CompanyResponseDTO
        assertEquals(savedUser.getId(), savedCompany.getIdDoRepresentante());
    }

    @Test
    void deleteCompanySuccessfully() {
        User savedUser = createAndAuthenticateUser("user@test1.com");

        CompanyRequestDTO dto = new CompanyRequestDTO();
        dto.setNomeDaEmpresa("Lojas Petronio");
        dto.setTelefone("8199800433");
        dto.setCnpj("49.789.000/0091-68");
        dto.setAreaDeAtuacao("enxovado");
        dto.setRepresentanteDaEmpresaId(savedUser.getId());

        CompanyResponseDTO savedCompany = companyService.create(dto);
        assertNotNull(savedCompany);

        final Long companyId = savedCompany.getId();

        companyService.deleteById(companyId);

        assertThrows(EntityNotFoundException.class, () -> companyService.findById(companyId));
    }

    @Test
    void updateCompanySuccessfully() {
        User savedUser = createAndAuthenticateUser("user@test2.com");

        CompanyRequestDTO dto = new CompanyRequestDTO();
        dto.setNomeDaEmpresa("Lojas Petronio");
        dto.setTelefone("8199800444");
        dto.setCnpj("49.789.000/0091-99");
        dto.setAreaDeAtuacao("enxovado");
        dto.setRepresentanteDaEmpresaId(savedUser.getId());

        CompanyResponseDTO savedCompany = companyService.create(dto);

        assertNotNull(savedCompany);

        // Use setters to update the DTO for the update call
        dto.setNomeDaEmpresa("Lojas Updated");
        dto.setTelefone("00000000000");

        CompanyResponseDTO updatedCompany = companyService.update(savedCompany.getId(), dto);

        assertEquals("Lojas Updated", updatedCompany.getNomeDaEmpresa());
        assertEquals("00000000000", updatedCompany.getTelefone());
    }
}