package com.VA2ES.backend.controllers;

import com.VA2ES.backend.dto.UpdateNameDTO;
import com.VA2ES.backend.models.User;
import com.VA2ES.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/name")
    public ResponseEntity<?> updateName(@RequestBody @Valid UpdateNameDTO dto, @AuthenticationPrincipal User current) {

        User updatedUser = userService.updateName(current, dto.newName());

        return ResponseEntity.ok(Map.of(
                "message", "Nome atualizado com sucesso",
                "novoNome", updatedUser.getNome()
        ));

    }

    @GetMapping
    public ResponseEntity<List<User>> listarTodos() {
        return ResponseEntity.ok(userService.listarTodos());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuário não autenticado"));
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "nome", user.getNome(),
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
    }

}
