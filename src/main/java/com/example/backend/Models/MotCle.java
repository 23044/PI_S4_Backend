package com.example.backend.Models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotCle { // Keyword -> MotCle
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<String> mot; // word -> mot
    
}