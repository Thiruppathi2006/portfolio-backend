package com.thiru.portfoliobackend.config;

import com.thiru.portfoliobackend.entity.Admin;
import com.thiru.portfoliobackend.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataInitializer(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String username = System.getenv("ADMIN_USERNAME");
        String password = System.getenv("ADMIN_PASSWORD");

        if (username == null || password == null) {
            System.out.println("ADMIN_USERNAME or ADMIN_PASSWORD is not set.");
            return;
        }

        if (adminRepository.findByUsername(username).isEmpty()) {

            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));

            adminRepository.save(admin);

            System.out.println("Admin account created successfully.");
        } else {
            System.out.println("Admin account already exists.");
        }
    }
}