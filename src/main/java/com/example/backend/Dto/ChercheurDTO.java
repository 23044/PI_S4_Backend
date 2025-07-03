package com.example.backend.Dto;

import com.example.backend.Models.Chercheur;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChercheurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String specialite;
    private Chercheur.EncadrementType encadrement;

    // Constructor simple - PAS de relations
    public ChercheurDTO(Chercheur chercheur) {
        this.id = chercheur.getId();
        this.nom = chercheur.getNom();
        this.prenom = chercheur.getPrenom();
        this.email = chercheur.getEmail();
        this.specialite = chercheur.getSpecialite();
        this.encadrement = chercheur.getEncadrement();
    }
}
