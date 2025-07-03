package com.example.backend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Models.FileEntity;

@Repository
public interface FileInfoRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findAllByUserId(Long userId);

    List<FileEntity> findByUserId(Long userId);

}