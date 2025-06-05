package com.example.backend.Repositories;

import java.lang.reflect.Type;
// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Models.Etablissement;
@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Long> { 
    Type findByType(Etablissement.Type type); 
}
