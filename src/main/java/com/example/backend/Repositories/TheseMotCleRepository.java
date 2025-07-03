package com.example.backend.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.backend.Models.These;
import com.example.backend.Models.TheseMotCle;
import java.util.List;


public interface TheseMotCleRepository extends JpaRepository<TheseMotCle,Long>{



    // Optional<TheseMotCle> findById(Long id);
    // @Query("SELECT tmc FROM TheseMotCle tmc WHERE tmc.these = :these")
    
    TheseMotCle findByTheseId(Long theseId);
    
    
} 