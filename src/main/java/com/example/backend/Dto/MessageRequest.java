package com.example.backend.Dto;

public class MessageRequest {
    private String to;
    private String content;

    // Getters et setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
