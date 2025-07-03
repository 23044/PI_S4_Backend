package com.example.backend.Models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Commentaire { // Comment -> Commentaire
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private These these; // Thesis -> These

    @ManyToOne
    private Users utilisateur; // user -> utilisateur

    private String texte; // text -> texte

    private LocalDate dateCommentaire; // commentDate -> dateCommentaire
}