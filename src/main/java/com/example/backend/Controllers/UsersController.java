package com.example.backend.Controllers;

import com.example.backend.Models.Users;
import com.example.backend.Dto.UserDTO;
import com.example.backend.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // API pour voir le profil utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        Users user = usersService.getUserById(id);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }

    // API pour obtenir tous les utilisateurs
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<Users> users = usersService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // API pour obtenir la liste des directeurs (ou encadrants)
    @GetMapping("/directeurs")
    public ResponseEntity<List<UserDTO>> getDirecteurs() {
        List<Users> directeurs = usersService.getUsersByRole("Directeur");
        List<UserDTO> directeurDTOs = directeurs.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(directeurDTOs);
    }

    // API facultative : obtenir le rôle d’un utilisateur
    @GetMapping("/{id}/role")
    public ResponseEntity<String> getUserRole(@PathVariable Long id) {
        String role = usersService.getUserRole(id);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }

    
}
