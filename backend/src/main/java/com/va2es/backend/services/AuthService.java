package com.va2es.backend.services;

import com.va2es.backend.dto.RegisterDTO;
import com.va2es.backend.models.User;
import com.va2es.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email já cadastrado.");
        }
        String encriptedPassword = new BCryptPasswordEncoder().encode(registerDTO.getPassword());

        User user = new User(registerDTO.getEmail(), encriptedPassword, registerDTO.getRole(), registerDTO.getNome());
        return this.userRepository.save(user);
    }

}
