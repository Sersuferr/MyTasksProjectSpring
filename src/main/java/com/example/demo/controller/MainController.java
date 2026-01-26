package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("user", principal.getAttributes());
        }
        return "index";
    }

    @GetMapping("/user/profile")
    public String userProfile(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }

        Map<String, Object> attributes = principal.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        model.addAttribute("name", name);
        model.addAttribute("login", attributes.get("login"));
        model.addAttribute("id", attributes.get("id") != null ? attributes.get("id") : attributes.get("sub"));
        model.addAttribute("email", email);
        model.addAttribute("avatar", attributes.get("avatar_url"));

        logger.info("Пользователь {} просматривает профиль", email);

        return "user/profile";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null) {
            return "redirect:/";
        }

        String email = principal.getAttribute("email");
        logger.info("Администратор {} вошел в админ-панель", email);

        return "admin/dashboard";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        String error = request.getParameter("error");
        if (error != null) {
            model.addAttribute("error", "Ошибка аутентификации");
            logger.error("Ошибка аутентификации пользователя");
        }
        return "login";
    }
}