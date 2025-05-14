package com.esprit.ftn.Controllers;

import com.esprit.ftn.dto.AuthResponse;
import com.esprit.ftn.dto.SuperAdminRegistrationRequest;
import com.esprit.ftn.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-super-admin")
    public ResponseEntity<AuthResponse> createSuperAdmin(@RequestBody SuperAdminRegistrationRequest request) {
        AuthResponse response = adminService.createSuperAdmin(request);
        return ResponseEntity.ok(response);
    }
}