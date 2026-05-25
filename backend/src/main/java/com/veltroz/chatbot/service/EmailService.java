package com.veltroz.chatbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String mailFrom;
    private final String adminEmail;

    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.username:}") String mailFrom,
                        @Value("${app.admin.email}") String adminEmail) {
        this.mailSender = mailSender;
        this.mailFrom = mailFrom;
        this.adminEmail = adminEmail;
    }

    public void sendAdminNotification(String subject, String body) {
        if (adminEmail == null || adminEmail.isBlank()) {
            logger.warn("Skipping admin notification because app.admin.email is not configured.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (mailFrom != null && !mailFrom.isBlank()) {
                message.setFrom(mailFrom);
            }
            message.setTo(adminEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("Admin notification email sent to {}", adminEmail);
        } catch (Exception ex) {
            logger.warn("Unable to send admin notification email. Continuing without failing the request.", ex);
        }
    }
}
