package com.veltroz.chatbot.service;
import org.springframework.context.annotation.Lazy;
import com.veltroz.chatbot.security.JwtUtil;
import com.veltroz.chatbot.dto.AuthRequest;
import com.veltroz.chatbot.dto.AuthResponse;
import com.veltroz.chatbot.dto.RegisterRequest;
import com.veltroz.chatbot.model.User;
import com.veltroz.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthService(

            UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        emailService.sendAdminNotification(
                "New Veltroz user registered",
                "A new user signed up:\n\n" +
                        "Name: " + user.getUsername() + "\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "Registered at: " + Instant.now() + "\n"
        );

        return new AuthResponse(token, user.getEmail(), user.getUsername());
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setLastLoginAt(Instant.now());
            userRepository.save(user);

            String token = jwtUtil.generateToken(user.getEmail());
            emailService.sendAdminNotification(
                    "Veltroz user login",
                    "User logged in:\n\n" +
                            "Name: " + user.getUsername() + "\n" +
                            "Email: " + user.getEmail() + "\n" +
                            "Login at: " + Instant.now() + "\n"
            );

            return new AuthResponse(token, user.getEmail(), user.getUsername());
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password", ex);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}
