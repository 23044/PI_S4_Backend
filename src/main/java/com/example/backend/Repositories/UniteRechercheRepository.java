package com.example.backend.Repositories;

import com.example.backend.Models.UniteRecherche; // ResearchUnit -> UniteRecherche

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniteRechercheRepository extends JpaRepository<UniteRecherche, Long> {
    List<UniteRecherche> findByEtablissementId(Long etablissementId);

}
