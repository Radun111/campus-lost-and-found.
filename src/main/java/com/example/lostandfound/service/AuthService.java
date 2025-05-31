package com.example.lostandfound.service;

import com.example.lostandfound.dto.AuthResponse;
import com.example.lostandfound.entity.*;
import com.example.lostandfound.exception.*;
import com.example.lostandfound.repository.*;
import com.example.lostandfound.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.lostandfound.exception.AuthenticationFailedException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(String username, String password, String email, String roleName) {
        // Validate input
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already in use");
        }

        // Get role
        String fullRoleName = "ROLE_" + roleName.toUpperCase();
        Role role = roleRepository.findByName(fullRoleName)
                .orElseThrow(() -> new RoleNotFoundException("Role " + roleName + " not found"));

        // Create user
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(role)
                .build();

        // Save user and generate tokens
        User savedUser = userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());

        String accessToken = jwtUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getId());

        return buildAuthResponse(savedUser, accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse login(String username, String password) {
        try {
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Get user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate tokens
            String accessToken = jwtUtil.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

            return buildAuthResponse(user, accessToken, refreshToken.getToken());
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtil.generateToken(
                            userDetailsService.loadUserByUsername(user.getUsername())
                    );
                    return buildAuthResponse(user, newAccessToken, refreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Invalid refresh token"));
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())  // Changed from .clone() to .username()
                .email(user.getEmail())
                .role(user.getRole().getName().replace("ROLE_", ""))
                .build();
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenService.deleteByUserId(userId);
    }
}