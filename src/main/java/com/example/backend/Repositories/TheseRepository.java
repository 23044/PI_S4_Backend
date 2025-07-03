package com.example.backend.Repositories;

import com.example.backend.Models.These;

import java.lang.classfile.ClassFile.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TheseRepository extends JpaRepository<These, Long> {
    These findByDoctorantId(Long doctorantId);
    // Optional<These> findById(Long id);
    List<These> findByStatut(These.Statut statut);
     List<These> findByChercheurId(Long chercheurId);
    //  @Query("SELECT t FROM These t WHERE t.motCles LIKE %:keyword%")
    //  List<These> findByMotClesContaining(@Param("keyword") String keyword);
}