package com.va2es.backend.services;

import com.va2es.backend.models.User;
import com.va2es.backend.repositories.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateName(User current, String newName) {
        current.setNome(newName);
        return userRepository.save(current);
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }
}