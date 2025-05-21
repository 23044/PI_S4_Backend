package com.example.backend.Models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objet;
    private LocalDateTime dateEvenement;

    @ManyToOne
    @JoinColumn(name = "doctorant_id")
    private Doctorants doctorant;
}