package com.inventory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String toAddress, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toAddress);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("[Email] Sent successfully to " + toAddress);
        } catch (Exception e) {
            System.err.println("[Email] Failed to send to " + toAddress + ": " + e.getMessage());
        }
    }
}
