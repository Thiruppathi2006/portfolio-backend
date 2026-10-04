package com.thiru.portfoliobackend.repository;

import com.thiru.portfoliobackend.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository
        extends JpaRepository<ContactMessage, Long> {
}