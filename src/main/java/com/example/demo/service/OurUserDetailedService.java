package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class OurUserDetailedService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.isAccountNonLocked()) {
            if (user.getLockTime() != null &&
                    user.getLockTime().plusHours(24).isBefore(LocalDateTime.now())) {
                user.setAccountNonLocked(true);
                user.setFailedAttempt(0);
                user.setLockTime(null);
                userRepository.save(user);
                log.info("Account {} automatically unlocked", username);
            } else {
                throw new RuntimeException("Account is locked");
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                user.isAccountNonLocked(),
                getAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public User registerUser(String username, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Transactional
    public void increaseFailedAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user != null) {
            int newFailAttempts = user.getFailedAttempt() + 1;
            user.setFailedAttempt(newFailAttempts);

            if (newFailAttempts >= 5) {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
                log.warn("Account {} locked due to 5 failed attempts", user.getUsername());
            }

            userRepository.save(user);
        }
    }

    @Transactional
    public void resetFailedAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user != null) {
            user.setFailedAttempt(0);
            userRepository.save(user);
        }
    }
}