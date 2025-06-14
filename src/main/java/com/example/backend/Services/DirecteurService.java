package com.example.backend.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Dto.DirecteurDTO;
import com.example.backend.Models.Directeur;
import com.example.backend.Repositories.DirecteurRepository;

@Service
public class DirecteurService {

    @Autowired
    private DirecteurRepository directeurRepository;

    public List<DirecteurDTO> getAllDirecteursDTO() {
        return directeurRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Directeur> getDirecteurById(Long id) {
        return directeurRepository.findById(id);
    }

    public Optional<Directeur> getDirecteurByUserId(Long userId) {
        return directeurRepository.findByUtilisateurId(userId);
    }

    public Directeur saveDirecteur(Directeur directeur) {
        return directeurRepository.save(directeur);
    }

    public boolean deleteDirecteur(Long id) {
        return directeurRepository.findById(id)
                .map(directeur -> {
                    directeurRepository.delete(directeur);
                    return true;
                }).orElse(false);
    }

    public DirecteurDTO mapToDTO(Directeur directeur) {
        DirecteurDTO dto = new DirecteurDTO();
        dto.setId(directeur.getId());
        dto.setDomaine(directeur.getDomaine());
        dto.setAnneeDiplome(directeur.getAnneeDiplome());

        if (directeur.getUtilisateur() != null) {
            dto.setNomUtilisateur(directeur.getUtilisateur().getFirstName());
            dto.setEmailUtilisateur(directeur.getUtilisateur().getEmail());

            if (directeur.getUtilisateur().getEtablissement() != null) {
                dto.setEtablissementNom(directeur.getUtilisateur().getEtablissement().getNom());
            }
        }

        return dto;
    }

}