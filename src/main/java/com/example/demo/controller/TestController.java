
package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public String userProfile() {
        return "User profile endpoint";
    }

    @GetMapping("/moderator/content")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public String moderateContent() {
        return "Moderator content endpoint";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String manageUsers() {
        return "Admin users endpoint";
    }

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Public hello endpoint";
    }
    @GetMapping("/debug/auth")
    public Map<String, Object> debugAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> info = new HashMap<>();

        if (auth != null) {
            info.put("authenticated", auth.isAuthenticated());
            info.put("username", auth.getName());
            info.put("authorities", auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            info.put("principal", auth.getPrincipal().toString());
        } else {
            info.put("authenticated", false);
            info.put("message", "No authentication found");
        }

        return info;
    }
    private String getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return "User: " + auth.getName() +
                    ", Authorities: " + auth.getAuthorities();
        }
        return "No user authenticated";
    }
}