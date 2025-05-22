package com.example.lostandfound.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token; // JWT token to return
}