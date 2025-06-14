package com.example.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Dto.UniteRechercheDTO;
import com.example.backend.Models.UniteRecherche;
import com.example.backend.Repositories.UniteRechercheRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UniteRechercheService {

    @Autowired
    private UniteRechercheRepository uniteRechercheRepository;

    public List<UniteRechercheDTO> getAllUnitesRecherche() {
        return uniteRechercheRepository.findAll().stream()
                .map(unite -> {
                    UniteRechercheDTO dto = new UniteRechercheDTO();
                    dto.setId(unite.getId());
                    dto.setNom(unite.getNom());
                    dto.setAcronyme(unite.getAcronyme());
                    dto.setDomaine(unite.getDomaine());
                    dto.setEtablissementNom(unite.getEtablissement().getNom());
                    return dto;
                }).collect(Collectors.toList());
    }

    public Optional<UniteRecherche> getUniteRechercheById(Long id) {
        return uniteRechercheRepository.findById(id);
    }

    public UniteRecherche saveUniteRecherche(UniteRecherche uniteRecherche) {
        return uniteRechercheRepository.save(uniteRecherche);
    }

    public Optional<UniteRecherche> updateUniteRecherche(Long id, UniteRecherche uniteRechercheDetails) {
        return uniteRechercheRepository.findById(id)
                .map(uniteRecherche -> {
                    uniteRecherche.setNom(uniteRechercheDetails.getNom());
                    uniteRecherche.setAcronyme(uniteRechercheDetails.getAcronyme());
                    uniteRecherche.setDomaine(uniteRechercheDetails.getDomaine());
                    uniteRecherche.setEtablissement(uniteRechercheDetails.getEtablissement());
                    return uniteRechercheRepository.save(uniteRecherche);
                });
    }

    public boolean deleteUniteRecherche(Long id) {
        return uniteRechercheRepository.findById(id)
                .map(uniteRecherche -> {
                    uniteRechercheRepository.delete(uniteRecherche);
                    return true;
                }).orElse(false);
    }

    public List<UniteRecherche> getUnitesRechercheByEtablissementId(Long etablissementId) {
        return uniteRechercheRepository.findByEtablissementId(etablissementId);
    }
}