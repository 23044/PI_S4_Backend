package com.example.backend.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private Users expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Users destinataire;

    private String objet;
    private String contenu;
    private LocalDateTime dateEnvoi = LocalDateTime.now();
    private boolean lu = false;

    @ManyToOne
    @JoinColumn(name = "these_id")
    private These these;
}
