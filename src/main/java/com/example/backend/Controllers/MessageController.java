package com.example.backend.Controllers;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.Dto.ConversationResponse;
import com.example.backend.Dto.MessageRequest;
import com.example.backend.Dto.MessageResponse;
import com.example.backend.Models.Message;
import com.example.backend.Models.Users;
import com.example.backend.Repositories.MessageRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Services.MessageService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // @PostMapping("/send")
    // public ResponseEntity<?> sendMessage(@RequestBody Map<String, String>
    // payload) {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // String senderEmail = auth.getName();

    // String to = payload.get("to");
    // String content = payload.get("content");

    // Users sender = userRepository.findByEmail(senderEmail).orElseThrow();
    // Users receiver = userRepository.findByEmail(to).orElseThrow();

    // Message message = new Message();
    // message.setSender(sender);
    // message.setReceiver(receiver);
    // message.setContent(content);

    // return ResponseEntity.ok(messageRepository.save(message));
    // }
    @Transactional
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String senderEmail = auth.getName();

        Optional<Users> senderOpt = userRepository.findByEmail(senderEmail);
        Optional<Users> receiverOpt = userRepository.findByEmail(request.getTo());

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "L'expéditeur ou le destinataire est introuvable"));
        }

        Message message = new Message();
        message.setSender(senderOpt.get());
        message.setReceiver(receiverOpt.get());
        message.setContent(request.getContent());
        messageRepository.save(message);

        return ResponseEntity.ok(
                new MessageResponse(
                        senderOpt.get().getEmail(),
                        receiverOpt.get().getEmail(),
                        message.getContent(),
                        message.getTimestamp()));
    }

    // @GetMapping("/inbox")
    // public ResponseEntity<?> getInbox() {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // String email = auth.getName();

    // List<Message> messages =
    // messageRepository.findByReceiverEmailOrderByTimestampDesc(email);

    // // convertir vers DTO lisible
    // List<MessageResponse> response = messages.stream().map(msg -> new
    // MessageResponse(
    // msg.getSender().getEmail(),
    // msg.getReceiver().getEmail(),
    // msg.getContent(),
    // msg.getTimestamp())).toList();

    // return ResponseEntity.ok(response);
    // }
    @GetMapping("/inbox")
    public ResponseEntity<?> getInbox() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Message> messages = messageRepository.findByReceiverEmailOrderByTimestampDesc(email);

        List<MessageResponse> response = messages.stream()
                .map(MessageResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Message> lastMessages = messageRepository.findLatestMessagesGroupedByUser(email);

        List<ConversationResponse> conversations = lastMessages.stream().map(msg -> {
            String contact = msg.getSender().getEmail().equals(email)
                    ? msg.getReceiver().getEmail()
                    : msg.getSender().getEmail();
            return new ConversationResponse(contact, msg.getContent(), msg.getTimestamp());
        }).toList();

        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(@RequestParam String with) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MessageResponse> conversation = messageRepository.findConversation(email, with);
        return ResponseEntity.ok(conversation);
    }

    // @PostMapping("/send-file")
    // public ResponseEntity<?> sendFile(@RequestParam("to") String toEmail,
    // @RequestParam("file") MultipartFile file)
    // throws IOException {
    // String senderEmail =
    // SecurityContextHolder.getContext().getAuthentication().getName();
    // Optional<Users> senderOpt = userRepository.findByEmail(senderEmail);
    // Optional<Users> receiverOpt = userRepository.findByEmail(toEmail);

    // if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND)
    // .body(Map.of("error", "Utilisateur introuvable"));
    // }

    // // Sauvegarde fichier local
    // String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    // String uploadDir = "uploads/";
    // Path uploadPath = Paths.get(uploadDir);
    // if (!Files.exists(uploadPath)) {
    // Files.createDirectories(uploadPath);
    // }
    // Path filePath = uploadPath.resolve(fileName);
    // Files.copy(file.getInputStream(), filePath,
    // StandardCopyOption.REPLACE_EXISTING);

    // Message message = new Message();
    // message.setSender(senderOpt.get());
    // message.setReceiver(receiverOpt.get());
    // message.setContent("/uploads/" + fileName);
    // message.setRead(false);
    // message.setTimestamp(LocalDateTime.now());

    // return ResponseEntity.ok(messageRepository.save(message));
    // }

}
