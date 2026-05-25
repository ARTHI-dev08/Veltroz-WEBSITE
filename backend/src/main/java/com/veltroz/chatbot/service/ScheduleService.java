package com.veltroz.chatbot.service;


import com.veltroz.chatbot.dto.ScheduleCallRequest;
import com.veltroz.chatbot.model.ScheduleCall;
import com.veltroz.chatbot.repository.ScheduleCallRepository; // Assuming you have this repository
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class ScheduleService {

    private final ScheduleCallRepository scheduleCallRepository;

    public ScheduleService(ScheduleCallRepository scheduleCallRepository) {
        this.scheduleCallRepository = scheduleCallRepository;
    }

    public ScheduleCall saveRequest(ScheduleCallRequest request, String userEmail) {
        ScheduleCall scheduleCall = new ScheduleCall();
        scheduleCall.setFullName(request.getFullName());
        scheduleCall.setContactEmail(request.getEmail());
        scheduleCall.setPhone(request.getPhone());
        scheduleCall.setMessage(request.getMessage());
        scheduleCall.setUserEmail(userEmail);
        scheduleCall.setCreatedAt(Instant.now());

        return scheduleCallRepository.save(scheduleCall);
    }
}
