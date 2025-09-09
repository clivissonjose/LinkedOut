package com.va2es.backend.services;

import com.va2es.backend.models.User;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateName(User current, String newName) {
        current.setNome(newName);
        return userRepository.save(current);
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    @Transactional
    public void changeUserRole(String email, String password, UserRole newRole) {
        User user = (User) userRepository.findByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("Usuário não encontrado com o e-mail fornecido.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }
}