package com.example.backend.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Models.Message;
import com.example.backend.Models.Users;
import com.example.backend.Repositories.MessageRepository;

@Service
public class MessageService {

    // @Autowired
    // private MessageRepository messageRepository;

    // public Message sendMessage(Users sender, Users receiver, String content) {
    //     Message message = new Message();
    //     message.setSender(sender);
    //     message.setReceiver(receiver);
    //     message.setContent(content);
    //     return messageRepository.save(message);
    // }

    // public List<Message> getInbox(Long receiverId) {
    //     return messageRepository.findByReceiverIdOrderByTimestampDesc(receiverId);
    // }

    // public List<Message> getConversation(Long senderId, Long receiverId) {
    //     return messageRepository.findBySenderIdAndReceiverIdOrderByTimestamp(senderId, receiverId);
    // }
}
