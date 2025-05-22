package com.example.lostandfound.dto;

import lombok.Data;

@Data // Lombok annotation for getters/setters
public class AuthRequest {
    private String username; // Login username
    private String password; // Login password
}