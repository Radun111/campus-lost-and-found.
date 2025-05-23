package com.example.lostandfound.dto;

public class AuthResponse {
    private String token;

    // No-args constructor (required by Spring)
    public AuthResponse() {
    }

    // Constructor that accepts token
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter and Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}