package com.example.lostandfound.dto;

import lombok.Data;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private String locationFound;
    private String status;
    private String reportedBy; // Username of reporter
}