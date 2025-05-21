package com.example.backend.Models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class These {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    private String titre;
    private String resume;
    private LocalDate dateSoumission;
    private LocalDate dateInscription;
    private LocalDate dateFinVisee;
    private LocalDate dateSoutenance;
    private String langue;
    private String urlPdf;
    private String motCles;
    private String fichierThese;
    private String etatThese;
    @Enumerated(EnumType.STRING)
    private Statut statut;

    public enum Statut {
        SOUMISE, EN_COURS, VALIDEE, ARCHIVEE
    }

    @OneToOne
    @JoinColumn(name = "doctorant_id")
    private Doctorants doctorant;

    @OneToMany(mappedBy = "these")
    private List<Validation> validations;
}