package com.example.backend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Dto.FileInfo;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findAllByUserId(Long userId);
}