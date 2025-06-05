package com.example.backend.Dto;

import java.time.LocalDate;
import com.example.backend.Models.These;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheseDTO {
    private Long id;
    private String titre;
    private String resume;
    private LocalDate dateSoumission;
    private LocalDate dateInscription;
    private LocalDate dateFinVisee;
    private LocalDate dateSoutenance;
    private String langue;
    private String urlPdf;
    private String motCles;
    private String fichierThese;
    private String etatThese;
    private These.Statut statut;
    private Long doctorantId; // Simple ID instead of full object
    private Long chercheurId; // Simple ID instead of full object

    // Constructor from entity (for responses)
    public TheseDTO(These these) {
        this.id = these.getId();
        this.titre = these.getTitre();
        this.resume = these.getResume();
        this.dateSoumission = these.getDateSoumission();
        this.dateInscription = these.getDateInscription();
        this.dateFinVisee = these.getDateFinVisee();
        this.dateSoutenance = these.getDateSoutenance();
        this.langue = these.getLangue();
        this.urlPdf = these.getUrlPdf();
        this.motCles = these.getMotCles();
        this.fichierThese = these.getFichierThese();
        this.etatThese = these.getEtatThese();
        this.statut = these.getStatut();
        this.doctorantId = these.getDoctorant() != null ? these.getDoctorant().getId() : null;
        this.chercheurId = these.getChercheur() != null ? these.getChercheur().getId() : null;
    }
}
