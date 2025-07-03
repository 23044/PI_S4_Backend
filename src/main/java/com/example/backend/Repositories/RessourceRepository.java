package com.example.backend.Repositories;

import com.example.backend.Models.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    
    // Trouver toutes les ressources ajoutées par un utilisateur
    List<Ressource> findByUserId(Long userId);
    
    // Trouver les ressources par titre (recherche)
    @Query("SELECT r FROM Ressource r WHERE r.titre LIKE %:titre%")
    List<Ressource> findByTitreContaining(@Param("titre") String titre);
    
    // Trouver les ressources par description (recherche)
    @Query("SELECT r FROM Ressource r WHERE r.description LIKE %:description%")
    List<Ressource> findByDescriptionContaining(@Param("description") String description);
    
    // Trouver toutes les ressources triées par titre
    @Query("SELECT r FROM Ressource r ORDER BY r.titre ASC")
    List<Ressource> findAllOrderByTitre();
}
