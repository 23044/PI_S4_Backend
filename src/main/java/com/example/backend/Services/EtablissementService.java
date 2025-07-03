package com.example.backend.Services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Dto.EtablissementDTO;
import com.example.backend.Models.Etablissement;
import com.example.backend.Models.UniteRecherche;
import com.example.backend.Repositories.EtablissementRepository;

@Service
public class EtablissementService {

    @Autowired
    private EtablissementRepository etablissementRepository;

    public List<EtablissementDTO> getAllEtablissementsDTO() {
        return etablissementRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Etablissement> getEtablissementById(Long id) {
        return etablissementRepository.findById(id);
    }

    public Etablissement saveEtablissement(Etablissement etablissement) {
        return etablissementRepository.save(etablissement);
    }

    public Optional<Etablissement> updateEtablissement(Long id, Etablissement etablissementDetails) {
        return etablissementRepository.findById(id)
                .map(etablissement -> {
                    etablissement.setNom(etablissementDetails.getNom());
                    etablissement.setPays(etablissementDetails.getPays());
                    etablissement.setType(etablissementDetails.getType());
                    return etablissementRepository.save(etablissement);
                });
    }

    public boolean deleteEtablissement(Long id) {
        return etablissementRepository.findById(id)
                .map(etablissement -> {
                    etablissementRepository.delete(etablissement);
                    return true;
                }).orElse(false);
    }

    public List<Etablissement> getEtablissementsByType(String type) {
        return etablissementRepository.findByType(type);
    }

    public EtablissementDTO mapToDTO(Etablissement etab) {
        EtablissementDTO dto = new EtablissementDTO();
        dto.setId(etab.getId());
        dto.setNom(etab.getNom());
        dto.setPays(etab.getPays());
        dto.setType(etab.getType());

        // On ajoute uniquement les noms des unités
        List<String> nomsUnites = etab.getUnitesRecherche().stream()
                .map(UniteRecherche::getNom)
                .collect(Collectors.toList());

        dto.setUnitesRechercheNoms(nomsUnites);

        return dto;
    }
}
