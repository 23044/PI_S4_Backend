package com.example.backend.Repositories;

import com.example.backend.Models.Doctorants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DoctorantRepository extends JpaRepository<Doctorants, Long> {
    Doctorants findByUserId(Long userId);
}