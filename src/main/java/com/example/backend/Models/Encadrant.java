package com.example.backend.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Encadrant { // Supervisor -> Superviseur
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;
    private String specialite; // specialty -> specialite

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users utilisateur; // user -> utilisateur
}