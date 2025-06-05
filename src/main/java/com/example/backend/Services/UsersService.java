package com.example.backend.Services;

import com.example.backend.Models.Chercheur;
import com.example.backend.Models.Doctorants;
import com.example.backend.Models.Users;
import com.example.backend.Models.Role;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Repositories.TheseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TheseRepository theseRepository;

    // Returns the list of PhD students supervised by a given researcher
    public List<Doctorants> getDoctorantsByChercheur(Chercheur chercheur) {
        return theseRepository.findByChercheurId(chercheur.getId())
                .stream()
                .map(these -> these.getDoctorant())
                .collect(Collectors.toList());
    }

    // Returns a user by ID
    public Users getUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);
        return user.orElse(null);
    }

    // Returns all users
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Returns users with a specific role
    public List<Users> getUsersByRole(String roleString) {
        Role role = Role.valueOf(roleString.toUpperCase());
        return userRepository.findByRole(role);
    }

    // Returns the role of a user
    public String getUserRole(Long id) {
        Users user = getUserById(id);
        return user != null ? user.getRole().toString() : null;
    }
}
