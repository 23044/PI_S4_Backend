package com.example.backend.Repositories;

import java.util.Optional;

import com.example.backend.Models.Encadrant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EncadrantRepository extends JpaRepository<Encadrant,Long>{
    // Optional<Encadrant> findByName(String name );
        Optional<Encadrant> findByUtilisateurId(Long userId);

}

