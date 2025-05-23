package com.example.lostandfound.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated unique ID

    private String name; // Item name (e.g., "Wallet", "Laptop")
    private String description; // Detailed description
    private String locationFound; // Where item was found

    @Enumerated(EnumType.STRING)
    private ItemStatus status; // LOST, FOUND, CLAIMED

    @ManyToOne
    private User reportedBy; // Who reported the item
}

enum ItemStatus {
    LOST, FOUND, CLAIMED
}