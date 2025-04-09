package com.spotify.spotify_backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private Double amount;
    private LocalDate payDate;
    private LocalDate ngayhethan;

    private String status; // success, pending, failed
}
