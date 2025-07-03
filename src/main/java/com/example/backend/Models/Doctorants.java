package com.example.backend.Models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctorants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    // Relation ManyToOne vers Docteur (un doctorant a un seul docteur)
    // @ManyToOne(optional = false)
    // @JoinColumn(name = "id_directeur", nullable = false)
    // private Directeur directeur;
    private String numeroInscription; // registrationNumber -> numeroInscription
    private Integer anneeInscription; // registrationYear -> anneeInscription

    // Relation OneToOne vers Users (chaque doctorant correspond à un user)
    // @OneToOne
    @OneToOne
    // @JsonBackReference
    @JoinColumn(name = "user_id", unique = true) // unique = true recommandé
    private Users user;

    @ManyToOne
    @JoinColumn(name = "unite_recherche_id") // research_unit_id -> unite_recherche_id
    private UniteRecherche uniteRecherche; // researchUnit -> uniteRecherche

    @ManyToOne
    @JoinColumn(name = "directeur_id", nullable = false)
    private Directeur directeur;

    @ManyToMany
    @JoinTable(name = "doctorant_encadrants", joinColumns = @JoinColumn(name = "doctorant_id"), inverseJoinColumns = @JoinColumn(name = "encadrant_id"))
    private List<Directeur> encadrants;

    public List<Directeur> getEncadrantsEffectifs() {
        if (encadrants == null || encadrants.isEmpty()) {
            if (directeur == null) {
                throw new RuntimeException("Le doctorant n'a pas de directeur affecté");
            }
            return List.of(directeur);
        }
        return encadrants;
    }

}
