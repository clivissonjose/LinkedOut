package com.VA2ES.backend.services;

import com.VA2ES.backend.comunicacao.dto.RegisterDTO;
import com.VA2ES.backend.models.User;
import com.VA2ES.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

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
