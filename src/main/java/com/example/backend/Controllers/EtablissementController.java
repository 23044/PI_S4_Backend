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

import com.example.backend.Dto.EtablissementDTO;
import com.example.backend.Models.Etablissement;
import com.example.backend.Services.EtablissementService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/etablissements")
public class EtablissementController {

    @Autowired
    private EtablissementService etablissementService;

    @GetMapping
    public ResponseEntity<List<EtablissementDTO>> getAllEtablissements() {
        return ResponseEntity.ok(etablissementService.getAllEtablissementsDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtablissementDTO> getEtablissementDTOById(@PathVariable Long id) {
        Optional<Etablissement> etab = etablissementService.getEtablissementById(id);

        if (etab.isPresent()) {
            EtablissementDTO dto = etablissementService.mapToDTO(etab.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Etablissement createEtablissement(@RequestBody Etablissement etablissement) {
        return etablissementService.saveEtablissement(etablissement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etablissement> updateEtablissement(@PathVariable Long id,
            @RequestBody Etablissement etablissementDetails) {
        Optional<Etablissement> updatedEtablissement = etablissementService.updateEtablissement(id,
                etablissementDetails);
        return updatedEtablissement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtablissement(@PathVariable Long id) {
        if (etablissementService.deleteEtablissement(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/type/{type}")
    public List<Etablissement> getEtablissementsByType(@PathVariable String type) {
        return etablissementService.getEtablissementsByType(type);
    }
}