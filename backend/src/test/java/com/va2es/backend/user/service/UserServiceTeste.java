package com.va2es.backend.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.va2es.backend.models.User;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.UserRepository;
import com.va2es.backend.services.UserService;
import com.va2es.backend.dto.RegisterDTO;
import com.va2es.backend.dto.AuthDTO;

import jakarta.persistence.EntityNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTeste {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void deveCriarUsuarioComSucesso() {
        User user = new User();
        user.setEmail("novo@teste.com");
        user.setPassword("senha123");
        user.setRole(UserRole.USER);
        user.setNome("Novo Usuário");

        User userSalvo = userRepository.save(user);

        assertNotNull(userSalvo);
        assertEquals("novo@teste.com", user.getEmail());
        assertTrue(userRepository.existsById(user.getId()));
    }


    @Test
    public void deveDeletarUsuarioComSucesso() {
        User user = new User("delete@teste.com", "senha123", UserRole.USER, "Delete Teste");
        user = userRepository.save(user);

        assertTrue(userRepository.existsById(user.getId()));

        userRepository.deleteById(user.getId());

        assertTrue(!userRepository.existsById(user.getId()));
    }

    @Test
    public void deveAtualizarNomeDoUsuarioComSucesso() {
        // Cria e salva o usuário
        User user = new User();
        user.setEmail("update@teste.com");
        user.setPassword("senha123");
        user.setRole(UserRole.USER);
        user.setNome("Nome Antigo");

        user = userRepository.save(user);

        // Atualiza o nome usando o service
        User atualizado = userService.updateName(user, "Nome Atualizado");

        // Verifica se o nome foi atualizado
        assertEquals("Nome Atualizado", atualizado.getNome());
    }
}
