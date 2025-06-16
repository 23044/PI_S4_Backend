package com.example.backend.Dto;

import java.time.LocalDateTime;

import com.example.backend.Models.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private String from;
    private String to;
    private String content;
    private LocalDateTime timestamp;

    public MessageResponse(Message m) {
        this.from = m.getSender().getEmail();
        this.to = m.getReceiver().getEmail();
        this.content = m.getContent();
        this.timestamp = m.getTimestamp();
    }
}