package com.example.backend.Models;

import jakarta.persistence.*;

@Entity
public class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String lien;
    private String description;

    @ManyToOne
    @JoinColumn(name = "ajoute_par")
    private Users user;
}