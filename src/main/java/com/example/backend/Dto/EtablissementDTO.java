package com.example.backend.Dto;

import java.util.List;

import com.example.backend.Models.Etablissement.Type;

public class EtablissementDTO {
    private Long id;
    private String nom;
    private String pays;
    private Type type;
    private List<String> unitesRechercheNoms; // juste les noms
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPays() {
        return pays;
    }
    public void setPays(String pays) {
        this.pays = pays;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public List<String> getUnitesRechercheNoms() {
        return unitesRechercheNoms;
    }
    public void setUnitesRechercheNoms(List<String> unitesRechercheNoms) {
        this.unitesRechercheNoms = unitesRechercheNoms;
    }
}