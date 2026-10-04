package com.thiru.portfoliobackend.controller;

import com.thiru.portfoliobackend.entity.ContactMessage;
import com.thiru.portfoliobackend.service.ContactMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    // Save a new contact message
    @PostMapping
    public ContactMessage createMessage(
            @RequestBody ContactMessage contactMessage) {

        return contactMessageService.saveMessage(contactMessage);
    }

    // Get all contact messages
    @GetMapping
    public List<ContactMessage> getAllMessages() {
        return contactMessageService.getAllMessages();
    }

    // Get one message
    @GetMapping("/{id}")
    public ContactMessage getMessage(@PathVariable Long id) {
        return contactMessageService.getMessageById(id);
    }

    // Delete a message
    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Long id) {

        contactMessageService.deleteMessage(id);

        return "Message deleted successfully";
    }
}