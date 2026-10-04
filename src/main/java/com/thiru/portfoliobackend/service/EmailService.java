package com.thiru.portfoliobackend.service;

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

    @Value("${MAIL_FROM}")
    private String mailFrom;

    @Value("${MAIL_TO}")
    private String mailTo;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendContactNotification(String name, String email, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailFrom);
            mailMessage.setTo(mailTo);
            mailMessage.setSubject("New Portfolio Contact Message");
            mailMessage.setText(String.format("Name: %s\nEmail: %s\n\nMessage:\n%s", name, email, message));

            mailSender.send(mailMessage);
            logger.info("Email notification sent for contact message from {}", email);
        } catch (Exception e) {
            logger.error("Failed to send email notification for contact message from {}", email, e);
            throw e;
        }
    }
}
