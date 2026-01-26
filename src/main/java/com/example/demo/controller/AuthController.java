package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.service.OurUserDetailedService;
import com.example.demo.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final OurUserDetailedService userService;

    @PostMapping("/init")
    public ResponseEntity<String> initUsers() {
        userService.registerUser("user", "password123", Role.USER);
        userService.registerUser("moderator", "password123", Role.MODERATOR);
        userService.registerUser("admin", "password123", Role.SUPER_ADMIN);
        return ResponseEntity.ok("Test users created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            userService.resetFailedAttempts(username);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);

            log.info("Successful login for user: {}", username);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            String username = loginRequest.get("username");
            try {
                userService.increaseFailedAttempts(username);
            } catch (Exception ex) {
                log.warn("User not found: {}", username);
            }

            log.warn("Failed login attempt for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}