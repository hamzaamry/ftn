package com.esprit.ftn.services;

import com.esprit.ftn.Repositories.UserRepository;
import com.esprit.ftn.dto.AuthResponse;
import com.esprit.ftn.dto.LoginRequest;
import com.esprit.ftn.entities.User;
import com.esprit.ftn.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getType().toString());

        return new AuthResponse(token, "Login successful");
    }
}