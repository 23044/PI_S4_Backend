package com.example.backend.Models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Encadrement { // Supervision -> Encadrement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "superviseur_id") // supervisor_id -> superviseur_id
    private Encadrant encadrant;      // supervisor -> superviseur

    @ManyToOne
    @JoinColumn(name = "doctorant_id")   // doctoral_student_id -> doctorant_id
    private Doctorants doctorant;         // DoctoralStudent -> Doctorant

    private LocalDate dateDebut;         // startDate -> dateDebut
    private LocalDate dateFin;           // endDate -> dateFin
}