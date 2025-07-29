package com.VA2ES.backend.services;

import com.VA2ES.backend.models.User;
import com.VA2ES.backend.repositories.UserRepository;
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
}