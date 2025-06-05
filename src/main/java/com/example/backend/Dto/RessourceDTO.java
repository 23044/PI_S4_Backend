package com.example.backend.Dto;

import com.example.backend.Models.Ressource;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RessourceDTO {
    private Long id;
    private String titre;
    private String lien;
    private String description;
    private Long ajouteParId; // Simple ID instead of full object
    // Constructor from entity (for responses)
    public RessourceDTO(Ressource ressource) {
        this.id = ressource.getId();
        this.titre = ressource.getTitre();
        this.lien = ressource.getLien();
        this.description = ressource.getDescription();
        this.ajouteParId = ressource.getUser() != null ? ressource.getUser().getId() : null;
    }
}
