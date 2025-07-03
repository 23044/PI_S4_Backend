package com.example.backend.Models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Etablissement { // Establishment -> Etablissement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom; // name -> nom
    private String pays; // country -> pays

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        Universite, // University -> Universite
        EcoleDoctorale // DoctoralSchool -> EcoleDoctorale
    }

    @OneToMany(mappedBy = "etablissement")
    // @JsonIgnore
    // @JsonManagedReference
    private Set<Users> utilisateurs; // users -> utilisateurs

    @OneToMany(mappedBy = "etablissement", cascade = CascadeType.ALL)
    // @JsonManagedReference
    private List<UniteRecherche> unitesRecherche;
}