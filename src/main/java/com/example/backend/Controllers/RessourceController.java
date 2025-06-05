package com.example.backend.Controllers;

import com.example.backend.Models.Ressource;
import com.example.backend.Dto.RessourceDTO;

import com.example.backend.Services.RessourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ressources")
public class RessourceController {

    @Autowired
    private RessourceService ressourceService;

    // GET /api/ressources - Liste de tous les documents ou liens utiles
    @GetMapping("/all")
    public ResponseEntity<?> getAllRessources() {
        try {
            List<Ressource> ressources = ressourceService.getAllRessources();
            List<RessourceDTO> ressourceDTOs = ressources.stream()
                    .map(RessourceDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                "ressources", ressourceDTOs,
                "count", ressourceDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
                ));
        }
    }

    // GET /api/ressources/{id} - Détail d'une ressource
    @GetMapping("/{id}")
    public ResponseEntity<?> getRessourceById(@PathVariable Long id) {
        try {
            Optional<Ressource> optionalRessource = ressourceService.getRessourceById(id);
            if (optionalRessource.isPresent()) {
                RessourceDTO ressourceDTO = new RessourceDTO(optionalRessource.get());
                return ResponseEntity.ok(ressourceDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "error", "Not Found",
                        "message", "Ressource not found with id: " + id
                    ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
                ));
        }
    }

    // POST /api/ressources - Ajouter une ressource (admin)
    @PostMapping
    public ResponseEntity<?> createRessource(@RequestBody(required = false) RessourceDTO createDTO) {
        // Validation du corps de la requête
        if (createDTO == null) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", "Bad Request",
                    "message", "Request body is required. Please provide resource data in JSON format.",
                    "example", Map.of(
                        "titre", "Guide de rédaction de thèse",
                        "lien", "https://example.com/guide.pdf",
                        "description", "Guide complet pour la rédaction d'une thèse",
                        "ajouteParId", 1
                    )
                ));
        }

        // Validation des champs obligatoires
        if (createDTO.getTitre() == null || createDTO.getTitre().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", "Bad Request",
                    "message", "Le titre est obligatoire"
                ));
        }

        if (createDTO.getAjouteParId() == null) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", "Bad Request",
                    "message", "L'ID de l'utilisateur est obligatoire"
                ));
        }

        try {
            Ressource createdRessource = ressourceService.createRessource(createDTO);
            RessourceDTO ressourceDTO = new RessourceDTO(createdRessource);

            return ResponseEntity.status(HttpStatus.CREATED).body(ressourceDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "error", "Bad Request",
                    "message", e.getMessage()
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
                ));
        }
    }

    // DELETE /api/ressources/{id} - Supprimer une ressource (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRessource(@PathVariable Long id) {
        try {
            boolean deleted = ressourceService.deleteRessource(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of(
                    "message", "Ressource deleted successfully",
                    "deletedId", id
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "error", "Not Found",
                        "message", "Ressource not found with id: " + id
                    ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
                ));
        }
    }

    // GET /api/ressources/search?titre=... - Rechercher par titre
    @GetMapping("/search")
    public ResponseEntity<?> searchRessources(@RequestParam(required = false) String titre,
                                             @RequestParam(required = false) String description) {
        try {
            List<Ressource> ressources;

            if (titre != null && !titre.trim().isEmpty()) {
                ressources = ressourceService.searchRessourcesByTitre(titre);
            } else if (description != null && !description.trim().isEmpty()) {
                ressources = ressourceService.searchRessourcesByDescription(description);
            } else {
                ressources = ressourceService.getAllRessources();
            }

            List<RessourceDTO> ressourceDTOs = ressources.stream()
                    .map(RessourceDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                "ressources", ressourceDTOs,
                "count", ressourceDTOs.size(),
                "searchCriteria", Map.of(
                    "titre", titre != null ? titre : "",
                    "description", description != null ? description : ""
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
                ));
        }
    }

    // GET /api/ressources/user/{userId} - Ressources ajoutées par un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRessourcesByUser(@PathVariable Long userId) {
        try {
            List<Ressource> ressources = ressourceService.getRessourcesByUserId(userId);
            List<RessourceDTO> ressourceDTOs = ressources.stream()
                    .map(RessourceDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                "userId", userId,
                "ressources", ressourceDTOs,
                "count", ressourceDTOs.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
                ));
        }
    }
}
