package com.veltroz.chatbot.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateConversationTitleRequest {

    @NotBlank
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
