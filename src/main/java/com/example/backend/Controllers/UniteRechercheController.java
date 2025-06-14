package com.example.backend.Controllers;

import com.example.backend.Dto.UniteRechercheDTO;
import com.example.backend.Models.UniteRecherche;
import com.example.backend.Services.UniteRechercheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/unites-recherche")
public class UniteRechercheController {

    @Autowired
    private UniteRechercheService uniteRechercheService;

    @GetMapping
    public List<UniteRechercheDTO> getAllUnitesRecherche() {
        return uniteRechercheService.getAllUnitesRecherche();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniteRecherche> getUniteRechercheById(@PathVariable Long id) {
        Optional<UniteRecherche> uniteRecherche = uniteRechercheService.getUniteRechercheById(id);
        return uniteRecherche.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UniteRecherche createUniteRecherche(@RequestBody UniteRecherche uniteRecherche) {
        return uniteRechercheService.saveUniteRecherche(uniteRecherche);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniteRecherche> updateUniteRecherche(@PathVariable Long id, @RequestBody UniteRecherche uniteRechercheDetails) {
        Optional<UniteRecherche> updatedUniteRecherche = uniteRechercheService.updateUniteRecherche(id, uniteRechercheDetails);
        return updatedUniteRecherche.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniteRecherche(@PathVariable Long id) {
        if (uniteRechercheService.deleteUniteRecherche(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/etablissement/{etablissementId}")
    public List<UniteRecherche> getUnitesRechercheByEtablissementId(@PathVariable Long etablissementId) {
        return uniteRechercheService.getUnitesRechercheByEtablissementId(etablissementId);
    }
}
