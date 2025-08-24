package com.va2es.backend.student.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.va2es.backend.dto.StudentRequestDTO;
import com.va2es.backend.dto.StudentResponseDTO;
import com.va2es.backend.models.Student;
import com.va2es.backend.models.User;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.StudentRepository;
import com.va2es.backend.repositories.UserRepository;
import com.va2es.backend.services.StudentService;

import jakarta.persistence.EntityNotFoundException;

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

    
}
