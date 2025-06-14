package com.example.backend.Repositories;

import com.example.backend.Models.Directeur;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirecteurRepository extends JpaRepository<Directeur, Long> {
    Optional<Directeur> findById(Long id);

    Optional<Directeur> findByUtilisateurId(Long userId);

    // Optional<Docteur> findByEmail(String email );
    // Optional<Docteur> findByPhoneNumber(String phoneNumber );
    // Optional<Docteur> findByAddress(String address );
    // Optional<Docteur> findBySpecialty(String specialty );

}
