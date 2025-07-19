package com.VA2ES.backend.comunicacao.controllers;

import com.VA2ES.backend.comunicacao.dto.AuthDTO;
import com.VA2ES.backend.comunicacao.dto.RegisterDTO;
import com.VA2ES.backend.models.User;
import com.VA2ES.backend.repositories.UserRepository;
import com.VA2ES.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AuthService authService;

    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
        AuthService authService) {
            this.authenticationManager = authenticationManager;
            this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity  login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
            authDTO.getEmail(), authDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDto) {

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.getPassword());

       // User newUser = new User(registerDto.getEmail(), encryptedPassword, registerDto.getRole(), registerDto.getNome());
        //  public User(String email, String encriptedPassword, UserRole role, String nome) {
        User newUser = this.authService.register(registerDto);

      //  this.userRepository.save(newUser);

        return  ResponseEntity.ok("Usuário registrado com sucesso");
    }

}
