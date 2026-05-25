package com.veltroz.chatbot.dto;

import java.time.Instant;

public class MessageDto {
    private Long id;
    private Long conversationId;
    private String sender;
    private String content;
    private Instant timestamp;

    public MessageDto() {
    }

    public MessageDto(Long id, Long conversationId, String sender, String content, Instant timestamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
