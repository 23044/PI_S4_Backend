package com.example.backend.Services;

import com.example.backend.Models.Agenda;
import com.example.backend.Models.Doctorants;
import com.example.backend.Dto.AgendaDTO;
import com.example.backend.Repositories.AgendaRepository;
import com.example.backend.Repositories.DoctorantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private DoctorantRepository doctorantRepository;

    // Obtenir tous les événements à venir d'un doctorant
    public List<Agenda> getUpcomingEventsByDoctorantId(Long doctorantId) {
        return agendaRepository.findUpcomingEventsByDoctorantId(doctorantId, LocalDateTime.now());
    }

    // Obtenir tous les événements d'un doctorant (passés et futurs)
    public List<Agenda> getAllEventsByDoctorantId(Long doctorantId) {
        return agendaRepository.findByDoctorantId(doctorantId);
    }

    // Obtenir un événement par ID
    public Optional<Agenda> getEventById(Long id) {
        return agendaRepository.findById(id);
    }

    // Créer un nouvel événement
    public Agenda createEvent(AgendaDTO createDTO) {
        // Vérifier que le doctorant existe
        Optional<Doctorants> doctorant = doctorantRepository.findById(createDTO.getDoctorantId());
        if (doctorant.isEmpty()) {
            throw new RuntimeException("Doctorant not found with id: " + createDTO.getDoctorantId());
        }

        Agenda agenda = new Agenda();
        agenda.setObjet(createDTO.getObjet());
        agenda.setDateEvenement(createDTO.getDateEvenement());
        agenda.setDoctorant(doctorant.get());

        return agendaRepository.save(agenda);
    }

    // Mettre à jour un événement
    public Agenda updateEvent(Long id, AgendaDTO updateDTO) {
        Optional<Agenda> optionalAgenda = agendaRepository.findById(id);
        if (optionalAgenda.isEmpty()) {
            throw new RuntimeException("Event not found with id: " + id);
        }

        Agenda agenda = optionalAgenda.get();

        // Mettre à jour les champs si fournis
        if (updateDTO.getObjet() != null) {
            agenda.setObjet(updateDTO.getObjet());
        }
        if (updateDTO.getDateEvenement() != null) {
            agenda.setDateEvenement(updateDTO.getDateEvenement());
        }
        if (updateDTO.getDoctorantId() != null) {
            Optional<Doctorants> doctorant = doctorantRepository.findById(updateDTO.getDoctorantId());
            if (doctorant.isPresent()) {
                agenda.setDoctorant(doctorant.get());
            } else {
                throw new RuntimeException("Doctorant not found with id: " + updateDTO.getDoctorantId());
            }
        }

        return agendaRepository.save(agenda);
    }

    // Supprimer un événement
    public boolean deleteEvent(Long id) {
        Optional<Agenda> optionalAgenda = agendaRepository.findById(id);
        if (optionalAgenda.isPresent()) {
            agendaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtenir les événements dans une période donnée
    public List<Agenda> getEventsByDoctorantIdAndDateRange(Long doctorantId, LocalDateTime startDate, LocalDateTime endDate) {
        return agendaRepository.findEventsByDoctorantIdAndDateRange(doctorantId, startDate, endDate);
    }

    // Obtenir tous les événements à venir (pour tous les doctorants)
    public List<Agenda> getAllUpcomingEvents() {
        return agendaRepository.findAllUpcomingEvents(LocalDateTime.now());
    }
}
