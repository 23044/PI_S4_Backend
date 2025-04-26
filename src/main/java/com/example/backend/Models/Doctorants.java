package com.example.backend.Models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String sujetThese;

    private LocalDate dateInscription;

    private LocalDate dateSoutenance;

    private String fichierThese;

    private String etatThese;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
