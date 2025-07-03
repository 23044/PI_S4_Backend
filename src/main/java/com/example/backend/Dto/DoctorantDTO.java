package com.example.backend.Dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorantDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String numeroMatricule;
    private LocalDate dateNaissance;
    private String nationalite;
    private String telephone;

    // Constructor simple - PAS de relations
    public DoctorantDTO(com.example.backend.Models.Doctorants doctorant) {
        this.id = doctorant.getId();
        this.nom = doctorant.getNom();
        this.prenom = doctorant.getPrenom();
        this.email = doctorant.getEmail();
        this.numeroMatricule = doctorant.getNumeroMatricule();
        this.dateNaissance = doctorant.getDateNaissance();
        this.nationalite = doctorant.getNationalite();
        this.telephone = doctorant.getTelephone();
    }
}
