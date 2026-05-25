package com.veltroz.chatbot.repository;

import com.veltroz.chatbot.model.ScheduleCall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleCallRepository extends JpaRepository<ScheduleCall, Long> {
}
