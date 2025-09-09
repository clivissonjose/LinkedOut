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
        dto.setFullName("João Silva");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setCpf("12345678900");
        dto.setPhone("9999999999");
        dto.setCourse("Ciência da Computação");
        dto.setCurrentPeriod(4);
        dto.setAcademicSummary("Bom aluno");
        dto.setUserId(savedUser.getId());

        // executa service
        StudentResponseDTO response = studentService.create(dto);

        // validações
        assertNotNull(response);
        assertEquals("João Silva", response.getFullName());
        assertEquals("12345678900", response.getCpf());

        // verifica persistência no banco
        assertTrue(studentRepository.existsByCpf("12345678900"));
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setFullName("Carlos Souza");
        dto.setBirthDate(LocalDate.of(2002, 3, 3));
        dto.setCpf("98765432100");
        dto.setPhone("6666666668");
        dto.setCourse("Medicina");
        dto.setCurrentPeriod(1);
        dto.setAcademicSummary("Resumo");
        dto.setUserId(999L); // ID inexistente

        assertThrows(EntityNotFoundException.class, () -> studentService.create(dto));
    }

    @Test
    public void deletarEstudanteComSucesso() {
        // cria e salva o usuário
        User user = new User(
                "user@test2.com",
                "password123",
                UserRole.ADMIN,
                "João de Teste"
        );
        User savedUser = userRepository.save(user);

        // cria DTO do estudante
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setFullName("João Silva");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setCpf("12345678880");
        dto.setPhone("9999999999");
        dto.setCourse("Ciência da Computação");
        dto.setCurrentPeriod(4);
        dto.setAcademicSummary("Bom aluno");
        dto.setUserId(savedUser.getId());

        StudentResponseDTO response = studentService.create(dto);

        assertNotNull(response);
        assertEquals("João Silva", response.getFullName());

        // Simula login no SecurityContext
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(savedUser, null, savedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String cpf = response.getCpf();
        assertTrue(studentRepository.existsByCpf(cpf));

        // deleta o estudante
        studentService.delete(response.getId());

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
        dto.setFullName("João Silva");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setCpf("12345600880");
        dto.setPhone("9999999999");
        dto.setCourse("Ciência da Computação");
        dto.setCurrentPeriod(4);
        dto.setAcademicSummary("Bom aluno");
        dto.setUserId(savedUser.getId());

        StudentResponseDTO response = studentService.create(dto);

        assertNotNull(response);
        assertEquals("João Silva", response.getFullName());

        // busca o estudante por ID
        StudentResponseDTO found = studentService.findById(response.getId());
        assertNotNull(found);
        assertEquals(response.getId(), found.getId());
        assertEquals(response.getFullName(), found.getFullName());
        assertEquals(response.getCpf(), found.getCpf());
    }

    @Test
    public void atualizarEstudanteComSucesso() {
        // cria e salva o usuário
        User user = new User(
                "user@test7.com",
                "password123",
                UserRole.ADMIN,
                "João de Teste"
        );
        User savedUser = userRepository.save(user);

        // cria DTO do estudante
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setFullName("João Silva");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setCpf("12345678955");
        dto.setPhone("9999999990");
        dto.setCourse("Ciência da Computação");
        dto.setCurrentPeriod(4);
        dto.setAcademicSummary("Bom aluno");
        dto.setUserId(savedUser.getId());

        StudentResponseDTO response = studentService.create(dto);

        assertNotNull(response);
        assertEquals("João Silva", response.getFullName());

        // Simula login no SecurityContext
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(savedUser, null, savedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // atualiza o estudante
        dto.setFullName("João Silva Atualizado");
        dto.setCourse("Letras");
        StudentResponseDTO updatedResponse = studentService.update(response.getId(), dto);

        assertNotNull(updatedResponse);
        assertEquals("João Silva Atualizado", updatedResponse.getFullName());
        assertEquals("Letras", updatedResponse.getCourse());
    }

    @Test
    public void naoDevePermitirDeletarEstudanteSemAutenticacao() {
        // cria e salva o usuário
        User user = new User(
                "user@test3.com",
                "password123",
                UserRole.ADMIN,
                "João de Teste"
        );
        User savedUser = userRepository.save(user);

        // cria DTO do estudante
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setFullName("Maria Silva");
        dto.setBirthDate(LocalDate.of(2001, 2, 2));
        dto.setCpf("12312312399");
        dto.setPhone("9998887077");
        dto.setCourse("Engenharia");
        dto.setCurrentPeriod(3);
        dto.setAcademicSummary("Resumo");
        dto.setUserId(savedUser.getId());

        StudentResponseDTO response = studentService.create(dto);

        // 🔑 NÃO setamos o SecurityContextHolder aqui → simula usuário não autenticado

        assertThrows(
                org.springframework.security.access.AccessDeniedException.class,
                () -> studentService.delete(response.getId())
        );
    }
}
