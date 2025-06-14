package com.example.backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.Dto.EncadrantDTO;
import com.example.backend.Models.Encadrant;
import com.example.backend.Services.EncadrantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/encadrants")
public class EncadrantController {

    @Autowired
    private EncadrantService encadrantService;

    @GetMapping
    public ResponseEntity<List<EncadrantDTO>> getAllEncadrants() {
        return ResponseEntity.ok(encadrantService.getAllEncadrantsDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Encadrant> getEncadrantById(@PathVariable Long id) {
        Optional<Encadrant> encadrant = encadrantService.getEncadrantById(id);
        return encadrant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Encadrant createEncadrant(@RequestBody Encadrant encadrant) {
        return encadrantService.saveEncadrant(encadrant);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<Encadrant> updateEncadrant(@PathVariable Long id,
    // @RequestBody Encadrant encadrantDetails) {
    // Optional<Encadrant> updatedEncadrant = encadrantService.updateEncadrant(id,
    // encadrantDetails);
    // return updatedEncadrant.map(ResponseEntity::ok)
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEncadrant(@PathVariable Long id) {
        if (encadrantService.deleteEncadrant(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}