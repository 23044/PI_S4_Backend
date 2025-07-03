package com.example.backend.Services;

import com.example.backend.Models.Ressource;
import com.example.backend.Models.Users;
import com.example.backend.Dto.RessourceDTO;
import com.example.backend.Repositories.RessourceRepository;
import com.example.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RessourceService {

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    private UserRepository userRepository;

    // Obtenir toutes les ressources
    public List<Ressource> getAllRessources() {
        return ressourceRepository.findAllOrderByTitre();
    }

    // Obtenir une ressource par ID
    public Optional<Ressource> getRessourceById(Long id) {
        return ressourceRepository.findById(id);
    }

    // Créer une nouvelle ressource
    public Ressource createRessource(RessourceDTO createDTO) {
        // Vérifier que l'utilisateur existe
        Optional<Users> user = userRepository.findById(createDTO.getAjouteParId());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found with id: " + createDTO.getAjouteParId());
        }

        Ressource ressource = new Ressource();
        ressource.setTitre(createDTO.getTitre());
        ressource.setLien(createDTO.getLien());
        ressource.setDescription(createDTO.getDescription());
        ressource.setUser(user.get());

        return ressourceRepository.save(ressource);
    }

    // Supprimer une ressource
    public boolean deleteRessource(Long id) {
        Optional<Ressource> optionalRessource = ressourceRepository.findById(id);
        if (optionalRessource.isPresent()) {
            ressourceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtenir les ressources par utilisateur
    public List<Ressource> getRessourcesByUserId(Long userId) {
        return ressourceRepository.findByUserId(userId);
    }

    // Rechercher des ressources par titre
    public List<Ressource> searchRessourcesByTitre(String titre) {
        return ressourceRepository.findByTitreContaining(titre);
    }

    // Rechercher des ressources par description
    public List<Ressource> searchRessourcesByDescription(String description) {
        return ressourceRepository.findByDescriptionContaining(description);
    }
}
