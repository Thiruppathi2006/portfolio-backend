package com.thiru.portfoliobackend.service;

import com.thiru.portfoliobackend.entity.ContactMessage;
import com.thiru.portfoliobackend.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ContactMessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ContactMessageService.class);

    private final ContactMessageRepository contactMessageRepository;
    private final EmailService emailService;

    public ContactMessageService(ContactMessageRepository contactMessageRepository, EmailService emailService) {
        this.contactMessageRepository = contactMessageRepository;
        this.emailService = emailService;
    }

    public ContactMessage saveMessage(ContactMessage contactMessage) {
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        
        try {
            emailService.sendContactNotification(savedMessage.getName(), savedMessage.getEmail(), savedMessage.getMessage());
        } catch (Exception e) {
            logger.error("Failed to send email notification, but message was saved successfully.", e);
        }
        
        return savedMessage;
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAll();
    }

    public ContactMessage getMessageById(Long id) {
        return contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }
}