package com.veltroz.chatbot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ScheduleCallRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String message;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
