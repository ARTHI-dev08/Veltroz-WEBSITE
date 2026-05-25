package com.veltroz.chatbot.dto;

public class ConversationMessagesResponse {
    private MessageDto userMessage;
    private MessageDto assistantMessage;

    public ConversationMessagesResponse() {
    }

    public ConversationMessagesResponse(MessageDto userMessage, MessageDto assistantMessage) {
        this.userMessage = userMessage;
        this.assistantMessage = assistantMessage;
    }

    public MessageDto getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(MessageDto userMessage) {
        this.userMessage = userMessage;
    }

    public MessageDto getAssistantMessage() {
        return assistantMessage;
    }

    public void setAssistantMessage(MessageDto assistantMessage) {
        this.assistantMessage = assistantMessage;
    }
}
