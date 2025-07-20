package com.VA2ES.backend.comunicacao.controllers;

import com.VA2ES.backend.comunicacao.dto.AuthDTO;
import com.VA2ES.backend.comunicacao.dto.LoginResponseDTO;
import com.VA2ES.backend.comunicacao.dto.RegisterDTO;
import com.VA2ES.backend.models.User;
import com.VA2ES.backend.repositories.UserRepository;
import com.VA2ES.backend.security.TokenService;
import com.VA2ES.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AuthService authService;

    private UserRepository userRepository;

    private TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
        AuthService authService, UserRepository userRepository, TokenService tokenService) {
            this.authenticationManager = authenticationManager;
            this.authService = authService;
            this.userRepository = userRepository;
            this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity  login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
            authDTO.getEmail(), authDTO.getPassword());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok( new LoginResponseDTO(token));

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDto) {

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.getPassword());

        User newUser = this.authService.register(registerDto);


       // return  ResponseEntity.ok("Usuário registrado com sucesso");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Usuário registrado com sucesso",
                        "user", Map.of(
                                "email", newUser.getEmail(),
                                "nome", newUser.getNome(),
                                "role", newUser.getRole().toString()
                        )
                ));
    }

}
