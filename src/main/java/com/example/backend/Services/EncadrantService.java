package com.example.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Dto.EncadrantDTO;
import com.example.backend.Models.Encadrant;
import com.example.backend.Repositories.EncadrantRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EncadrantService {

    @Autowired
    private EncadrantRepository encadrantRepository;

    public List<EncadrantDTO> getAllEncadrantsDTO() {
        return encadrantRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Encadrant> getEncadrantById(Long id) {
        return encadrantRepository.findById(id);
    }

    public Optional<Encadrant> getEncadrantByUserId(Long userId) {
        return encadrantRepository.findByUtilisateurId(userId);
    }

    public Encadrant saveEncadrant(Encadrant encadrant) {
        return encadrantRepository.save(encadrant);
    }

    // public Optional<Encadrant> updateEncadrant(Long id, Encadrant
    // encadrantDetails) {
    // return encadrantRepository.findById(id)
    // .map(encadrant -> {
    // encadrant.setGrade(encadrantDetails.getGrade());
    // encadrant.setSpecialite(encadrantDetails.getSpecialite());
    // encadrant.setUser(encadrantDetails.getUser());
    // return encadrantRepository.save(encadrant);
    // });
    // }

    public boolean deleteEncadrant(Long id) {
        return encadrantRepository.findById(id)
                .map(encadrant -> {
                    encadrantRepository.delete(encadrant);
                    return true;
                }).orElse(false);
    }

    public EncadrantDTO mapToDTO(Encadrant encadrant) {
        EncadrantDTO dto = new EncadrantDTO();
        dto.setId(encadrant.getId());
        dto.setGrade(encadrant.getGrade());
        dto.setSpecialite(encadrant.getSpecialite());

        if (encadrant.getUtilisateur() != null) {
            dto.setNomUtilisateur(encadrant.getUtilisateur().getFirstName());
            dto.setEmailUtilisateur(encadrant.getUtilisateur().getEmail());

            if (encadrant.getUtilisateur().getEtablissement() != null) {
                dto.setEtablissementNom(encadrant.getUtilisateur().getEtablissement().getNom());
            }
        }

        return dto;
    }
}