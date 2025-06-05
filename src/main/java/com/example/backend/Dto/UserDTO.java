package com.example.backend.Dto;

import com.example.backend.Models.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private com.example.backend.Models.Users.Role role;

    // Constructor pour créer un DTO à partir d'une entité Users
    public UserDTO(com.example.backend.Models.Users user) {
        this.id = user.getId();
        this.prenom = user.getFirstName();
        this.nom = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
