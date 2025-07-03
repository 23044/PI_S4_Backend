package com.example.backend.Models;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Transient
    private List<String> motCles;
    // @ElementCollection
    private String motClesString;
    private String fichierThese;
    private String etatThese;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    public enum Statut {
        Soumise, // Submitted
        EnCours, // InProgress
        Validee, // Validated
        Archivee // Archived
    }

    @ManyToOne
    // @JsonBackReference
    // @JsonManagedReference
    // @JsonIgnore
    @JoinColumn(name = "doctorant_id")
    private Doctorants doctorant;

    @ManyToOne
    @JoinColumn(name = "superviseur_id")
    private Encadrant encadrant;

    @ManyToOne
    @JoinColumn(name = "etablissement_id")
    private Etablissement etablissement;

    @ManyToOne
    @JoinColumn(name = "unite_recherche_id")
    private UniteRecherche uniteRecherche;

@ManyToOne
    @JoinColumn(name = "chercheur_id")
    @JsonBackReference("chercheur-theses")
    private Chercheur chercheur;

    @OneToMany(mappedBy = "these", cascade = CascadeType.ALL)
    private List<Validation> validations;
    
}
