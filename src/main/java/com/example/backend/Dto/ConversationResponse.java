package com.example.backend.Dto;

// package com.example.backend.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ConversationResponse {
    private String contactEmail;
    private String lastMessage;
    private LocalDateTime timestamp;

    // public ConversationResponse(String contactEmail, String lastMessage, LocalDateTime timestamp) {
    //     this.contactEmail = contactEmail;
    //     this.lastMessage = lastMessage;
    //     this.timestamp = timestamp;
    // }
}
