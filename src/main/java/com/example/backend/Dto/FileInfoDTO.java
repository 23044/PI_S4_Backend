package com.example.backend.Dto;

import com.example.backend.Models.FileEntity;

public class FileInfoDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String filePath;

    public FileInfoDTO(FileEntity entity) {
        this.id = entity.getId();
        this.fileName = entity.getFileName();
        this.fileType = entity.getFileType();
        this.fileSize = entity.getFileSize();
        this.filePath = "/api/files/view/" + entity.getFileName(); // ou download
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Getters
}
