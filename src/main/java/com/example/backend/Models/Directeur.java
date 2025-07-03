package com.example.backend.Models;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Directeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String domaine; // field -> domaine
    private String anneeDiplome; // diplomaYear -> anneeDiplome

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users utilisateur;

    @OneToMany(mappedBy = "directeur")
    private Set<Doctorants> doctorants;

}