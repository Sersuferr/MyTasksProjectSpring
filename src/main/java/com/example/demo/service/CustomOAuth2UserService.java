package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        logger.info("Пользователь аутентифицирован через {}: {}",
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = extractEmail(attributes, registrationId);

        List<String> roles = determineRoles(email);

        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        modifiedAttributes.put("roles", roles);

        return new DefaultOAuth2User(
                createAuthorities(roles),
                modifiedAttributes,
                getUsernameAttributeName(registrationId)
        );
    }

    private String extractEmail(Map<String, Object> attributes, String registrationId) {
        if ("github".equals(registrationId)) {
            return (String) attributes.get("email");
        } else if ("google".equals(registrationId)) {
            return (String) attributes.get("email");
        }
        return null;
    }

    private List<String> determineRoles(String email) {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        if (email != null && email.toLowerCase().contains("admin")) {
            roles.add("ROLE_ADMIN");
            logger.info("Пользователю {} назначена роль ADMIN", email);
        }

        return roles;
    }

    private Collection<? extends org.springframework.security.core.GrantedAuthority> createAuthorities(List<String> roles) {
        return roles.stream()
                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
                .toList();
    }

    private String getUsernameAttributeName(String registrationId) {
        if ("github".equals(registrationId)) {
            return "id";
        } else if ("google".equals(registrationId)) {
            return "sub";
        }
        return "name";
    }
}