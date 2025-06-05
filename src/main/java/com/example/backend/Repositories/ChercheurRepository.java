package com.example.backend.Repositories;

import com.example.backend.Models.Chercheur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChercheurRepository extends JpaRepository<Chercheur, Long> {
    Chercheur findByEmail(String email);
}
