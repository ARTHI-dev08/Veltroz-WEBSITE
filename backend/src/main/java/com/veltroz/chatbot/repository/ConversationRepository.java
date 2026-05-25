package com.veltroz.chatbot.repository;

import com.veltroz.chatbot.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserEmailOrderByUpdatedAtDesc(String userEmail);
    void deleteByUserEmail(String userEmail);
}
