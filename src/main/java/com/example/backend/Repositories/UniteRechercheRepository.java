package com.example.backend.Repositories;

import java.util.List; // ResearchUnit -> UniteRecherche
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Models.UniteRecherche;

@Repository
public interface UniteRechercheRepository extends JpaRepository<UniteRecherche, Long> {
    Optional<UniteRecherche> findById(Long id); 
    List<UniteRecherche> findByEtablissementId(Long etablissementId);

}
