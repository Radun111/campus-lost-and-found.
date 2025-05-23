package com.example.lostandfound.dto;

import lombok.Data;

@Data
public class ItemRequest {
    private String name;
    private String description;
    private String locationFound;
    private String status; // Frontend sends as string
    private Long reportedById; // Who's reporting this item
}