package com.example.backend.Repositories;

import com.example.backend.Models.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    
    // Trouver tous les événements d'un doctorant
    List<Agenda> findByDoctorantId(Long doctorantId);
    
    // Trouver les événements à venir d'un doctorant (triés par date)
    @Query("SELECT a FROM Agenda a WHERE a.doctorant.id = :doctorantId AND a.dateEvenement > :now ORDER BY a.dateEvenement ASC")
    List<Agenda> findUpcomingEventsByDoctorantId(@Param("doctorantId") Long doctorantId, @Param("now") LocalDateTime now);
    
    // Trouver les événements d'un doctorant dans une période donnée
    @Query("SELECT a FROM Agenda a WHERE a.doctorant.id = :doctorantId AND a.dateEvenement BETWEEN :startDate AND :endDate ORDER BY a.dateEvenement ASC")
    List<Agenda> findEventsByDoctorantIdAndDateRange(@Param("doctorantId") Long doctorantId, 
                                                     @Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate);
    
    // Trouver tous les événements à venir (pour tous les doctorants)
    @Query("SELECT a FROM Agenda a WHERE a.dateEvenement > :now ORDER BY a.dateEvenement ASC")
    List<Agenda> findAllUpcomingEvents(@Param("now") LocalDateTime now);
}
