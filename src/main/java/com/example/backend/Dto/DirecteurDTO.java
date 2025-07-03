package com.example.backend.Dto;

public class DirecteurDTO {
    private Long id;
    private String domaine;
    private String anneeDiplome;
    private String nomUtilisateur;
    private String emailUtilisateur;
    private String etablissementNom;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDomaine() {
        return domaine;
    }
    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }
    public String getAnneeDiplome() {
        return anneeDiplome;
    }
    public void setAnneeDiplome(String anneeDiplome) {
        this.anneeDiplome = anneeDiplome;
    }
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }
    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }
    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }
    public String getEtablissementNom() {
        return etablissementNom;
    }
    public void setEtablissementNom(String etablissementNom) {
        this.etablissementNom = etablissementNom;
    }
}