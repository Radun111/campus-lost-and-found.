package com.example.lostandfound.dto;

import com.example.lostandfound.entity.ItemStatus;
import lombok.Data;

@Data
public class ItemSearchCriteria {
    private ItemStatus status;
    private String location;
    private String nameContains;
}