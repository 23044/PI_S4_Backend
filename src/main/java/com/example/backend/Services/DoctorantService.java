package com.example.backend.Services;

import com.example.backend.Models.Doctorants;
import com.example.backend.Repositories.DoctorantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorantService {

    @Autowired
    private DoctorantRepository doctorantRepository;

    public Doctorants getDoctorantByUserId(Long userId) {
        return doctorantRepository.findByUserId(userId);
    }
}