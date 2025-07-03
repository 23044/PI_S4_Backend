package com.example.backend.Models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Users sender;

    @ManyToOne
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Users receiver;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean isRead = false;
}
