package com.example.backend.Dto;

import com.example.backend.Models.Users;

import java.util.List;

import com.example.backend.Models.Doctorants;
import com.example.backend.Models.These;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// import 

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private Users user;
    private Doctorants doctorant;
    private These these;
    private List<String> motCles;
}
