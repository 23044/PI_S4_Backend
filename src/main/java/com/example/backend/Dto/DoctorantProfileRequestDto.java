package com.example.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorantProfileRequestDto {
    @Data
    public static class UserDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String phoneNumber;
        private String birthDate;
        private String nationality;
        private Long etablissementId;
        private String etablissementNom;
    }

    @Data
    public static class DoctorantDto {
        private Long id;
        private String numeroInscription;
        private Long uniteRechercheId;
        private String uniteRechercheNom;
    }

    @Data
    public static class TheseDto {
        private Long id;
        private String titre;
        private String resume;
        private String dateSoumission;
        private String dateInscription;
        private String dateFinVisee;
        private String dateSoutenance;
        private String etatThese;
        private String statut;
    }

    private UserDto user;
    private DoctorantDto doctorant;
    private TheseDto these;
}