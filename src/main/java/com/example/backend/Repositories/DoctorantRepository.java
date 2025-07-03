package com.example.backend.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Models.Doctorants;

@Repository
public interface DoctorantRepository extends JpaRepository<Doctorants, Long> {
    Optional<Doctorants> findById(Long id);
    Doctorants findByUserId(Long userId);
}