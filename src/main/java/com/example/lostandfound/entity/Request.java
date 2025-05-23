package com.example.lostandfound.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.example.lostandfound.entity.RequestStatus;

@Data
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Item item; // Which item is being claimed

    @ManyToOne
    private User requester; // Who is making the claim

    private LocalDateTime requestDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;
}

