package com.veltroz.chatbot.service;

import com.veltroz.chatbot.dto.*;
import com.veltroz.chatbot.model.Conversation;
import com.veltroz.chatbot.model.Message;
import com.veltroz.chatbot.repository.ConversationRepository;
import com.veltroz.chatbot.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ChatService(ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    public List<ConversationDto> listConversations(String userEmail) {
        return conversationRepository.findByUserEmailOrderByUpdatedAtDesc(userEmail).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConversationDto createConversation(String userEmail, String title) {
        Conversation conversation = new Conversation();
        conversation.setUserEmail(userEmail);
        conversation.setTitle(title);
        Conversation saved = conversationRepository.save(conversation);
        return toDto(saved);
    }

    public List<MessageDto> listMessages(Long conversationId, String userEmail) {
        if (!belongsToUser(conversationId, userEmail)) {
            throw new IllegalArgumentException("Conversation not found");
        }
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConversationMessagesResponse addMessage(Long conversationId, CreateMessageRequest request, String userEmail) {
        if (!belongsToUser(conversationId, userEmail)) {
            throw new IllegalArgumentException("Conversation not found");
        }
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        Message userMessage = new Message();
        userMessage.setConversation(conversation);
        userMessage.setSender(request.getSender());
        userMessage.setContent(request.getContent());
        userMessage.setTimestamp(Instant.now());
        messageRepository.save(userMessage);

        Message assistantMessage = new Message();
        assistantMessage.setConversation(conversation);
        assistantMessage.setSender("ASSISTANT");
        assistantMessage.setContent(getAssistantReply(request.getContent()));
        assistantMessage.setTimestamp(Instant.now());
        messageRepository.save(assistantMessage);

        conversation.setUpdatedAt(Instant.now());
        conversationRepository.save(conversation);

        return new ConversationMessagesResponse(toDto(userMessage), toDto(assistantMessage));
    }

    @Transactional
    public void deleteAllConversations(String userEmail) {
        conversationRepository.deleteByUserEmail(userEmail);
    }

    @Transactional
    public ConversationDto updateConversationTitle(String userEmail, Long conversationId, String title) {
        if (!belongsToUser(conversationId, userEmail)) {
            throw new IllegalArgumentException("Conversation not found");
        }
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        conversation.setTitle(title);
        conversation.setUpdatedAt(Instant.now());
        Conversation saved = conversationRepository.save(conversation);
        return toDto(saved);
    }

    private boolean belongsToUser(Long conversationId, String userEmail) {
        return conversationRepository.findById(conversationId)
                .map(c -> c.getUserEmail().equals(userEmail))
                .orElse(false);
    }

    private ConversationDto toDto(Conversation conversation) {
        return new ConversationDto(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );
    }

    private MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getConversation().getId(),
                message.getSender(),
                message.getContent(),
                message.getTimestamp()
        );
    }

    private String getAssistantReply(String userText) {
        String text = userText.toLowerCase();
        if (text.matches(".*\\b(hi|hello|vanakam|hey|ai)\\b.*")) {
            return "Hello! I am Veltroz AI. I can provide details about Veltroz company, leadership, services, contact information, and mission.";
        }
        if (text.matches(".*\\b(name|what is your name|who are you|name means)\\b.*")) {
            return "Our company name is Veltroz — Vision Of Innovation. Veltroz is the technology brand for IT services, AI tech innovation, and robotics automation.";
        }
        if (text.matches(".*\\b(service|services|what do you provide|what do you offer|your service|what you do)\\b.*")) {
            return "Veltroz provides IT services and solutions, AI tech innovation, and robotics and automation.";
        }
        if (text.matches(".*\\b(contact|phone|reach|call|email|how can i contact|how to contact you)\\b.*")) {
            return "You can contact Veltroz at 8072713542 for sales, support, or partnership inquiries.";
        }
        if (text.matches(".*\\b(ceo|founder|leader|who leads|who is in charge)\\b.*")) {
            return "The company leaders are Vigneshwar P and Sabari Kannan R, who guide the vision and operations of Veltroz.";
        }
        if (text.matches(".*\\b(mission|vision|about your company|what is your company|why|purpose)\\b.*")) {
            return "Veltroz is focused on innovation: delivering IT services, AI technology solutions, and robotics automation to shape the future.";
        }
        if (text.matches(".*\\b(website|site|web page|online presence)\\b.*")) {
            return "This website introduces Veltroz and describes our services in IT, AI innovation, robotics, and automation.";
        }
        if (text.matches(".*\\b(hours|open|working hours|availability)\\b.*")) {
            return "Our support hours are Monday through Friday, 9:00 AM to 5:00 PM in our standard business schedule.";
        }
        if (text.matches(".*\\b(location|where are you|address|office)\\b.*")) {
            return "Veltroz operates across technology markets; contact 8072713542 and we will share the appropriate regional office or remote support details.";
        }
        if (text.matches(".*\\b(pricing|cost|quote|estimate)\\b.*")) {
            return "For pricing details, please contact us at 8072713542. We provide custom proposals depending on your project requirements.";
        }
        if (text.matches(".*\\b(career|jobs|hiring|join|work)\\b.*")) {
            return "Veltroz welcomes talent in IT, AI, robotics, and automation. Contact us if you are interested in career opportunities.";
        }
        if (text.matches(".*\\b(support|help|issue|problem)\\b.*")) {
            return "For support, reach out at 8072713542. We can assist with IT services, AI solutions, and automation projects.";
        }
        return "I am Veltroz AI. Ask me about Veltroz — Vision Of Innovation, our services, leadership, contact details, or mission.";
    }
}
