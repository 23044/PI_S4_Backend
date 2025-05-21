package com.example.backend.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Chercheur extends Users {
    private String specialite;

    @Enumerated(EnumType.STRING)
    private EncadrementType encadrement;

    public enum EncadrementType {
        ENCADRANT, DIRECTEUR
    }

    @ManyToOne
    @JoinColumn(name = "these_id")
    private These these;
}
