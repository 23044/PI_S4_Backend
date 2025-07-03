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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public LocalDateTime getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(LocalDateTime dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public Doctorants getDoctorant() {
        return doctorant;
    }

    public void setDoctorant(Doctorants doctorant) {
        this.doctorant = doctorant;
    }
}