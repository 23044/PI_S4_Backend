package com.example.backend.Repositories;

import com.example.backend.Models.Docteur;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocteurRepository extends JpaRepository<Docteur, Long> {
    Optional<Docteur> findById(Long id);
    // Optional<Docteur> findByEmail(String email );
    // Optional<Docteur> findByPhoneNumber(String phoneNumber );
    // Optional<Docteur> findByAddress(String address );
    // Optional<Docteur> findBySpecialty(String specialty );

}
