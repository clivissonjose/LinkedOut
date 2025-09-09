package com.va2es.backend.services;

import com.va2es.backend.dto.RegisterDTO;
import com.va2es.backend.models.User;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username);
    }

    public User register(RegisterDTO registerDTO) {
        UserDetails userDetails = this.userRepository.findByEmail(registerDTO.getEmail());

        if (userDetails != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
        }
        String encriptedPassword = new BCryptPasswordEncoder().encode(registerDTO.getPassword());

        // A role é definida como USER por padrão, direto no backend.
        User user = new User(registerDTO.getEmail(), encriptedPassword, UserRole.USER, registerDTO.getNome());

        return this.userRepository.save(user);
    }
}