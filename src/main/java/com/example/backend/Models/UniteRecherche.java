package com.example.backend.Models;

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
public class UniteRecherche { // ResearchUnit -> UniteRecherche
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom; // name -> nom
    private String acronyme; // acronym -> acronyme
    private String domaine; // domain -> domaine

    @ManyToOne
    // @JsonIgnore
    // @JsonBackReference
    private Etablissement etablissement;// Establishment -> Etablissement
}