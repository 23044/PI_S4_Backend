package com.example.backend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.Dto.MessageResponse;
import com.example.backend.Models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverEmailOrderByTimestampDesc(String receiverEmail);

    List<Message> findBySenderEmailAndReceiverEmailOrderByTimestamp(String sender, String receiver);

    // Dernier message avec chaque utilisateur
    @Query("SELECT m FROM Message m WHERE m.timestamp IN ( " +
            "SELECT MAX(m2.timestamp) FROM Message m2 " +
            "WHERE m2.sender.email = :email OR m2.receiver.email = :email " +
            "GROUP BY CASE WHEN m2.sender.email = :email THEN m2.receiver.email ELSE m2.sender.email END)")
    List<Message> findLatestMessagesGroupedByUser(@Param("email") String email);

    // Conversation avec un utilisateur
    @Query("SELECT new com.example.backend.Dto.MessageResponse(" +
            "m.sender.email, m.receiver.email, m.content, m.timestamp) " +
            "FROM Message m WHERE " +
            "(m.sender.email = :user1 AND m.receiver.email = :user2) " +
            "OR (m.sender.email = :user2 AND m.receiver.email = :user1) " +
            "ORDER BY m.timestamp")
    List<MessageResponse> findConversation(@Param("user1") String user1, @Param("user2") String user2);

}
