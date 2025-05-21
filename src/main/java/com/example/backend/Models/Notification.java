package com.example.backend.Models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;
    private LocalDateTime date;
    private boolean vue = false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Users destinataire;
}