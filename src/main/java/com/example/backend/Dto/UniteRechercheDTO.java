package com.example.backend.Dto;

public class UniteRechercheDTO {
    private Long id;
    private String nom;
    private String acronyme;
    private String domaine;
    private String etablissementNom; // juste le nom
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
    public String getAcronyme() {
        return acronyme;
    }
    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }
    public String getDomaine() {
        return domaine;
    }
    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }
    public String getEtablissementNom() {
        return etablissementNom;
    }
    public void setEtablissementNom(String etablissementNom) {
        this.etablissementNom = etablissementNom;
    }
}