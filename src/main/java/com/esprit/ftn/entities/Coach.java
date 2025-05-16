package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coaches")
@Data
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "verification_date")
    private java.util.Date verificationDate;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy; // The admin who verified this coach
}