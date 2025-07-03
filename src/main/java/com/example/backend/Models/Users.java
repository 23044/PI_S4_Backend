package com.example.backend.Models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastname;
    private int phoneNumber;
    private LocalDate birthdate;
    private String Nationality;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    public enum Role {
        administrateur,
        directeur,
        docteur,
        encadrant,
        doctorant
    }

    @ManyToOne
    @JoinColumn(name = "establishment_id")
    // @JsonBackReference
    // @JsonIgnore
    private Etablissement etablissement;

    @Column(nullable = false)
    // @JsonIgnore
    private String password;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    // @JsonIgnore
    public String resetToken;
     
    // @JsonIgnore
    private String verificationToken;

    @Builder.Default
    private boolean isVerified = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthdate;
    }

    public void setBirthDate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    @OneToOne(mappedBy = "user")
    // @JsonManagedReference
    // @JsonIgnore
    private Doctorants doctorant;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // @JsonIgnore
    private List<FileEntity> files;
}
