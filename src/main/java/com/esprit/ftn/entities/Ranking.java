package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rankings")
@Data
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Competition info
    @Column(name = "competition_title")
    private String competitionTitle;

    @Column(name = "competition_url")
    private String competitionUrl;

    // Event info
    @Column(name = "event_name")
    private String eventName;

    // Category info
    @Column(name = "category")
    private String category; // "dames" or "messieurs"

    @Column(name = "category_url")
    private String categoryUrl;

    @Column(name = "event_title")
    private String eventTitle; // "Séries"

    // Ranking details
    @Column(name = "place")
    private String place;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "nation")
    private String nation;

    @Column(name = "birth_year")
    private String birthYear;

    @Column(name = "club")
    private String club;

    @Column(name = "time")
    private String time;

    @Column(name = "points")
    private String points;

    @Column(name = "passage_time", length = 512)
    private String passageTime;
}