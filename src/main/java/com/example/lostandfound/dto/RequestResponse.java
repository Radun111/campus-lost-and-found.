package com.example.lostandfound.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RequestResponse {
    private Long id;                   // Request ID
    private String itemName;           // Name of requested item
    private String requesterName;      // Username of requester
    private LocalDateTime requestDate; // When request was made
    private String status;             // PENDING/APPROVED/REJECTED
}