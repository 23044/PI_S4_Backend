package com.example.backend.Controllers;

import com.example.backend.Models.Doctorants;
import com.example.backend.Services.DoctorantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctorant")
public class DoctorantController {

    @Autowired
    private DoctorantService doctorantService;

    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<Doctorants> getDoctorantDashboard(@PathVariable Long userId) {
        try {
            Doctorants doctorant = doctorantService.getDoctorantByUserId(userId);
            return ResponseEntity.ok(doctorant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}