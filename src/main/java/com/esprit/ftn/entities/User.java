package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private UserType type;

    public enum UserType {
        SUPER_ADMIN, ADMIN, COACH, ATHLETE
    }

    private String resetToken;

    private LocalDateTime resetTokenExpiry;
}