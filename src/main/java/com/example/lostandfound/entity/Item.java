package com.example.lostandfound.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String locationFound;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @ManyToOne
    private User reportedBy;
}