package com.example.lostandfound.service;

import com.example.lostandfound.dto.RegisterRequest;
import com.example.lostandfound.entity.User;
import com.example.lostandfound.entity.Role;
import com.example.lostandfound.exception.DuplicateUsernameException;
import com.example.lostandfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername().trim();
        String email = registerRequest.getEmail() != null ?
                registerRequest.getEmail().trim() : null;

        // Check for existing username
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username '" + username + "' already exists");
        }

        // Handle role
        String role = (registerRequest.getRole() != null &&
                !registerRequest.getRole().isBlank()) ?
                registerRequest.getRole().toUpperCase() : "USER";

        try {
            Role.valueOf(role); // Validate role
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role specified. Must be USER or ADMIN");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(email)
                .role(Role.valueOf(role))
                .build();

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username.trim());
    }
}