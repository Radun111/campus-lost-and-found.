package com.example.lostandfound.controller;

import com.example.lostandfound.dto.*;
import com.example.lostandfound.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getRole()
        );
        return ResponseEntity.ok(
                new ApiResponse<>("User registered successfully", response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(
                request.getUsername(),
                request.getPassword()
        );
        return ResponseEntity.ok(
                new ApiResponse<>("Login successful", response)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(
                new ApiResponse<>("Token refreshed", response)
        );
    }
}