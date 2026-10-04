package com.thiru.portfoliobackend.service;

import com.thiru.portfoliobackend.entity.Admin;
import com.thiru.portfoliobackend.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin saveAdmin(Admin admin) {
        admin.setPassword(
                passwordEncoder.encode(admin.getPassword()));

        return adminRepository.save(admin);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
