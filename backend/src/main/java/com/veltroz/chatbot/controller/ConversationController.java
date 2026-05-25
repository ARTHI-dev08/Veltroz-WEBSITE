package com.veltroz.chatbot.controller;

import com.veltroz.chatbot.dto.*;
import com.veltroz.chatbot.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*")
public class ConversationController {

    private final ChatService chatService;

    public ConversationController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<ConversationDto> getConversations() {
        return chatService.listConversations(getCurrentUserEmail());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationDto createConversation(@Valid @RequestBody CreateConversationRequest request) {
        return chatService.createConversation(getCurrentUserEmail(), request.getTitle());
    }

    @GetMapping("/{id}/messages")
    public List<MessageDto> getMessages(@PathVariable Long id) {
        return chatService.listMessages(id, getCurrentUserEmail());
    }

    @PostMapping("/{id}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ConversationMessagesResponse addMessage(@PathVariable Long id,
                                                   @Valid @RequestBody CreateMessageRequest request) {
        return chatService.addMessage(id, request, getCurrentUserEmail());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllConversations() {
        chatService.deleteAllConversations(getCurrentUserEmail());
    }

    @PatchMapping("/{id}")
    public ConversationDto updateConversationTitle(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateConversationTitleRequest request) {
        return chatService.updateConversationTitle(getCurrentUserEmail(), id, request.getTitle());
    }

    private String getCurrentUserEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "guest@veltroz.local";
        }
        return authentication.getName();
    }
}
