package com.example.backend.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Dto.DirecteurDTO;
import com.example.backend.Models.Directeur;
import com.example.backend.Services.DirecteurService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/directeurs")
public class DirecteurController {

    @Autowired
    private DirecteurService directeurService;

    @GetMapping
    public ResponseEntity<List<DirecteurDTO>> getAllDirecteurs() {
        return ResponseEntity.ok(directeurService.getAllDirecteursDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Directeur> getDirecteurById(@PathVariable Long id) {
        Optional<Directeur> directeur = directeurService.getDirecteurById(id);
        return directeur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Directeur createDirecteur(@RequestBody Directeur directeur) {
        return directeurService.saveDirecteur(directeur);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<Directeur> updateDirecteur(@PathVariable Long id,
    // @RequestBody Directeur directeurDetails) {
    // Optional<Directeur> updatedDirecteur = directeurService.updateDirecteur(id,
    // directeurDetails);
    // return updatedDirecteur.map(ResponseEntity::ok)
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirecteur(@PathVariable Long id) {
        if (directeurService.deleteDirecteur(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
