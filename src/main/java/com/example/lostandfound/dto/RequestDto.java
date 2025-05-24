package com.example.lostandfound.dto;

import lombok.Data;

@Data
public class RequestDto {
    private Long itemId;       // ID of the item being claimed
    private Long requesterId;  // ID of user making the request
    // Note: Status is not included - defaults to PENDING
}