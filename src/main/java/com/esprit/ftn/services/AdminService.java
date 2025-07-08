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
        // Vérifie si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Créer l'utilisateur
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setType(User.UserType.SUPER_ADMIN);

        User savedUser = userRepository.save(user);

        // Créer l'admin avec isSuperAdmin = true
        Admin admin = new Admin();
        admin.setUser(savedUser);
        admin.setSuperAdmin(true);
        adminRepository.save(admin); // 💡 Ajout important ici

        // Générer le token JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getType().toString());

        // Retourner la réponse
        return new AuthResponse(token, "Super admin created successfully");
    }
}