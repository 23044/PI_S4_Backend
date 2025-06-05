package com.example.backend.Dto;

import java.time.LocalDateTime;
import com.example.backend.Models.Agenda;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDTO {
    private Long id;
    private String objet;
    private LocalDateTime dateEvenement;
    private Long doctorantId; // Simple ID instead of full object

    // Constructor from entity (for responses)
    public AgendaDTO(Agenda agenda) {
        this.id = agenda.getId();
        this.objet = agenda.getObjet();
        this.dateEvenement = agenda.getDateEvenement();
        this.doctorantId = agenda.getDoctorant() != null ? agenda.getDoctorant().getId() : null;
    }
}
