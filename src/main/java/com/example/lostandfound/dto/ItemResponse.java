package com.example.lostandfound.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private String locationFound;
    private String status;
    private String reportedBy;
}