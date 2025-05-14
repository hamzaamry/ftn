package com.esprit.ftn.services;

import com.esprit.ftn.Repositories.AdminRepository;
import com.esprit.ftn.Repositories.UserRepository;
import com.esprit.ftn.dto.AuthResponse;
import com.esprit.ftn.dto.SuperAdminRegistrationRequest;
import com.esprit.ftn.entities.Admin;
import com.esprit.ftn.entities.User;
import com.esprit.ftn.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public AuthResponse createSuperAdmin(SuperAdminRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setType(User.UserType.SUPER_ADMIN);

        User savedUser = userRepository.save(user);

        // Create admin
        Admin admin = new Admin();
        admin.setUser(savedUser);
        admin.setSuperAdmin(true);
        adminRepository.save(admin);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getType().toString());

        return new AuthResponse(token, "Super admin created successfully");
    }
}