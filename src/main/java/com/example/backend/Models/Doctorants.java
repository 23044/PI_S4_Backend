package com.example.backend.Models;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "doctorants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctorants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    @Email
    @Column(unique = true)
    private String email;
    private String motDePasse;
    @Column(unique = true)
    private String numeroMatricule;
    private LocalDate dateNaissance;
    private String nationalite;
    private String telephone;
   

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonManagedReference
    private Users user;

    @ManyToOne
    @JoinColumn(name = "directeur_id", nullable = false)
    private Users directeur;

    @ManyToMany
    @JoinTable(name = "doctorant_encadrants", joinColumns = @JoinColumn(name = "doctorant_id"), inverseJoinColumns = @JoinColumn(name = "encadrant_id"))
    private List<Users> encadrants;

    public List<Users> getEncadrantsEffectifs() {
        if (encadrants == null || encadrants.isEmpty()) {
            if (directeur == null) {
                throw new RuntimeException("Le doctorant n'a pas de directeur affecté");
            }
            return List.of(directeur);
        }
        return encadrants;
    }
}
