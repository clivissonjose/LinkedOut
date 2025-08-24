package com.va2es.backend.student.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.va2es.backend.dto.StudentRequestDTO;
import com.va2es.backend.dto.StudentResponseDTO;
import com.va2es.backend.models.User;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.StudentRepository;
import com.va2es.backend.repositories.UserRepository;
import com.va2es.backend.services.StudentService;

import jakarta.persistence.EntityNotFoundException;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceTest {

    @Autowired
    private StudentRepository studentRepository;

   @Autowired
   private UserRepository userRepository;

    @Autowired
    private StudentService studentService;
    

  @Test
   public void deveCriarEstudanteComSucesso() {
    // cria e salva o usuário
    User user = new User(
            "user@test.com",
            "password123",
            UserRole.ADMIN,
            "João de Teste"
    );
    User savedUser = userRepository.save(user);

    // cria DTO do estudante
    StudentRequestDTO dto = new StudentRequestDTO();
    dto.fullName = "João Silva";
    dto.birthDate = LocalDate.of(2000, 1, 1);
    dto.cpf = "12345678900";
    dto.phone = "999999999";
    dto.course = "Ciência da Computação";
    dto.currentPeriod = 4;
    dto.academicSummary = "Bom aluno";
    dto.userId = savedUser.getId();

    // executa service
    StudentResponseDTO response = studentService.create(dto);

    // validações
    assertNotNull(response);
    assertEquals("João Silva", response.fullName);
    assertEquals("12345678900", response.cpf);

    // verifica persistência no banco
    assertTrue(studentRepository.existsByCpf("12345678900"));
  }
  
    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.fullName = "Carlos Souza";
        dto.birthDate = LocalDate.of(2002, 3, 3);
        dto.cpf = "98765432100";
        dto.phone = "666666666";
        dto.course = "Medicina";
        dto.currentPeriod = 1;
        dto.academicSummary = "Resumo";
        dto.userId = 999L; // ID inexistente

        assertThrows(EntityNotFoundException.class, () -> studentService.create(dto));
    }  


  @Test
  public void deletarEstudanteComSucesso() {
      // cria e salva o usuário
      User user = new User(
              "user@test2.com",
              "password123",
              UserRole.ADMIN, // admin consegue deletar qualquer estudante
              "João de Teste"
      );
      User savedUser = userRepository.save(user);

      // cria DTO do estudante
      StudentRequestDTO dto = new StudentRequestDTO();
      dto.fullName = "João Silva";
      dto.birthDate = LocalDate.of(2000, 1, 1);
      dto.cpf = "12345678880";
      dto.phone = "999999999";
      dto.course = "Ciência da Computação";
      dto.currentPeriod = 4;
      dto.academicSummary = "Bom aluno";
      dto.userId = savedUser.getId();

      StudentResponseDTO response = studentService.create(dto);

      assertNotNull(response);
      assertEquals("João Silva", response.fullName);

      // 🔑 Simula login no SecurityContext
      UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(savedUser, null, savedUser.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);

      String cpf = response.cpf;

      assertTrue(studentRepository.existsByCpf(cpf));

      // deleta o estudante
      studentService.delete(response.id);

      // verifica se foi deletado
      assertTrue(!studentRepository.existsByCpf(cpf));

    }

    @Test
    public void pegarEstudantePorIdComSucesso() {
      // cria e salva o usuário
      User user = new User(
              "user@test6.com",
              "password123",
              UserRole.ADMIN,
              "João de Teste"
      );
      User savedUser = userRepository.save(user);

      // cria DTO do estudante
      StudentRequestDTO dto = new StudentRequestDTO();
      dto.fullName = "João Silva";
      dto.birthDate = LocalDate.of(2000, 1, 1);
      dto.cpf = "12345600880";
      dto.phone = "999999999";
      dto.course = "Ciência da Computação";
      dto.currentPeriod = 4;
      dto.academicSummary = "Bom aluno";
      dto.userId = savedUser.getId();

      StudentResponseDTO response = studentService.create(dto);

      assertNotNull(response);
      assertEquals("João Silva", response.fullName);

      // busca o estudante por ID
      StudentResponseDTO found = studentService.findById(response.id);
      assertNotNull(found);
      assertEquals(response.id, found.id);
      assertEquals(response.fullName, found.fullName);
      assertEquals(response.cpf, found.cpf);

    }

    public void atualizarEstudanteComSucesso() {
        // cria e salva o usuário
        User user = new User(
                "user@test6.com",
                "password123",
                UserRole.ADMIN,
                "João de Teste"
        );
        User savedUser = userRepository.save(user);

        // cria DTO do estudante
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.fullName = "João Silva";
        dto.birthDate = LocalDate.of(2000, 1, 1);
        dto.cpf = "12345678955";
        dto.phone = "999999999";
        dto.course = "Ciência da Computação";
        dto.currentPeriod = 4;
        dto.academicSummary = "Bom aluno";
        dto.userId = savedUser.getId();

        StudentResponseDTO response = studentService.create(dto);

        assertNotNull(response);
        assertEquals("João Silva", response.fullName);

        // 🔑 Simula login no SecurityContext
      UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(savedUser, null, savedUser.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);

        // atualiza o estudante
        dto.fullName = "João Silva Atualizado";
        dto.course= "Letras";
        StudentResponseDTO updatedResponse = studentService.update(response.id, dto);

        assertNotNull(updatedResponse);
        assertEquals("João Silva Atualizado", updatedResponse.fullName);
        assertEquals("Letras", updatedResponse.course);
    }

}
