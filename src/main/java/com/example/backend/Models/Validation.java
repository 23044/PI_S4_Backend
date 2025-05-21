package com.example.backend.Models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateValidation;
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "these_id")
    private These these;

    @ManyToOne
    @JoinColumn(name = "valide_par")
    private Users user;
}