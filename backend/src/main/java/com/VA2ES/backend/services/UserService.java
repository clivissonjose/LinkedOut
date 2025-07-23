package com.VA2ES.backend.services;

import com.VA2ES.backend.models.User;
import com.VA2ES.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User updateName(User current, String newName) {
        current.setNome(newName);
        return userRepository.save(current);
    }
}