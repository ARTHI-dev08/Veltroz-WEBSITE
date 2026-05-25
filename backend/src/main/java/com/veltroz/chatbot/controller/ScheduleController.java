package com.veltroz.chatbot.controller;

import com.veltroz.chatbot.dto.ScheduleCallRequest;
import com.veltroz.chatbot.model.ScheduleCall;
import com.veltroz.chatbot.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleCall requestSchedule(@Valid @RequestBody ScheduleCallRequest request,
                                        Authentication authentication) {
        String email = null;
        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
        }
        return scheduleService.saveRequest(request, email);
    }
}
