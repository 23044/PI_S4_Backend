package com.example.backend.Repositories;

import com.example.backend.Models.Role;
import com.example.backend.Models.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByVerificationToken(String token);

    Optional<Users> findByResetToken(String resetToken);

    Optional<Users> findByUsername(String username);

    List<Users> findByRole(Role role);
}