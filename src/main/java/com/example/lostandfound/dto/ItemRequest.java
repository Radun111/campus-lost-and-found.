package com.example.lostandfound.dto;

import lombok.Data;

@Data
public class ItemRequest {
    private String name;
    private String description;
    private String locationFound;
    private String status;
    private Long reportedById; // Must match exactly what you're calling in ItemService
}