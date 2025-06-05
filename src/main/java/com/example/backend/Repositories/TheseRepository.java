package com.example.backend.Repositories;

import com.example.backend.Models.These;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheseRepository extends JpaRepository<These, Long> {
    These findByDoctorantId(Long doctorantId);
    List<These> findByStatut(These.Statut statut);
     List<These> findByChercheurId(Long chercheurId);
}