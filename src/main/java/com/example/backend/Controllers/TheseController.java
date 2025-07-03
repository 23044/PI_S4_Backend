package com.example.backend.Controllers;

import com.example.backend.Models.Doctorants;
import com.example.backend.Models.These;
import com.example.backend.Dto.TheseDTO;

import com.example.backend.Dto.DoctorantDTO;
import com.example.backend.Services.TheseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/theses")
public class TheseController {

    @Autowired
    private TheseService theseService;
    // Endpoint to get all theses
    @GetMapping("/all")
    public ResponseEntity<List<TheseDTO>> getAllTheses() {
        List<These> theses = theseService.getAllTheses();
        List<TheseDTO> theseDTOs = theses.stream()
                .map(TheseDTO::new) // Simple et propre
                .collect(Collectors.toList());
        return ResponseEntity.ok(theseDTOs);
    }
    // Endpoint to get a thesis by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getThesisById(@PathVariable Long id) {
        try {
            Optional<These> optionalThesis = theseService.getThesisById(id);
            if (optionalThesis.isPresent()) {
                TheseDTO theseDTO = new TheseDTO(optionalThesis.get());
                return ResponseEntity.ok(theseDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "error", "Not Found",
                        "message", "Thesis not found with id: " + id
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

    // Endpoint to get a thesis by doctorant ID
    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<?> getThesisByDoctorantId(@PathVariable Long doctorantId) {
        try {
            These thesis = theseService.getThesisByDoctorantId(doctorantId);
            if (thesis != null) {
                TheseDTO theseDTO = new TheseDTO(thesis);
                return ResponseEntity.ok(theseDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "error", "Not Found",
                        "message", "No thesis found for doctorant with id: " + doctorantId
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

    // Endpoint to update a thesis
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateThesis(@PathVariable Long id, @RequestBody TheseDTO updateDTO) {
        try {
            These updatedThesis = theseService.updateThesis(id, updateDTO);
            TheseDTO theseDTO = new TheseDTO(updatedThesis);
            System.out.println("these updated");
            return ResponseEntity.ok(theseDTO);
        } catch (Exception e) {
            System.out.println("Error: msg"+e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    //Endpoit to recupere une these par statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<These>> getThesisByStatut(@PathVariable These.Statut statut) {
        List<These> theses = theseService.getThesisByStatut(statut);
        return ResponseEntity.ok(theses);
    }
    // API pour obtenir les doctorants encadrés par un chercheur
    @GetMapping("/{chercheurId}/doctorants")
    public ResponseEntity<List<Doctorants>> getDoctorantsByChercheur(@PathVariable Long chercheurId) {
        List<Doctorants> doctorants = theseService.getDoctorantsByChercheurId(chercheurId);
        return ResponseEntity.ok(doctorants);
    }

    // @PutMapping("/update/thesedetails")
    // public ResponseEntity<?> updateTheseDetails(@RequestBody TheseDTO  request){
        // try {
            // Optional<These> t
        // }
    // }


}