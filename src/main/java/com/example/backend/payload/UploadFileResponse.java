package com.example.backend.payload;

import com.example.backend.Models.FileEntity;

public class UploadFileResponse {
    private FileEntity fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(FileEntity fileName2, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName2;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public FileEntity getFileName() {
        return fileName;
    }

    public void setFileName(FileEntity fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}