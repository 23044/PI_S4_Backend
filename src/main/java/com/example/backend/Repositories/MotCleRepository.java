package com.example.backend.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Models.MotCle;
import java.util.List;


public interface MotCleRepository extends JpaRepository<MotCle,Long>{

//  MotCle findByMotCle(MotCle motCle);
Optional<MotCle> findById(Long id);
} 
