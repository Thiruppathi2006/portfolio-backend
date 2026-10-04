package com.thiru.portfoliobackend.controller;

import com.thiru.portfoliobackend.security.JwtService;
import com.thiru.portfoliobackend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final JwtService jwtService;

    public AdminController(AdminService adminService, JwtService jwtService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

        String username = loginData.get("username");
        String password = loginData.get("password");

        return adminService.findByUsername(username)
                .filter(admin -> adminService.checkPassword(password, admin.getPassword()))
                .<ResponseEntity<?>>map(admin -> {
                    String token = jwtService.generateToken(admin.getUsername());
                    return ResponseEntity.ok(Map.of(
                            "message", "Login successful",
                            "token", token
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(
                        Map.of("message", "Invalid username or password")));
    }
}