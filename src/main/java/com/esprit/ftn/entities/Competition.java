package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "competitions")
@Data

public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String url;

    private String season;

    private LocalDate dateStart;

    private LocalDate dateEnd;
}
