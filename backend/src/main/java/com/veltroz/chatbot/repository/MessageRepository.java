package com.veltroz.chatbot.repository;

import com.veltroz.chatbot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByTimestampAsc(Long conversationId);
}
