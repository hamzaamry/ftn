package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "competition_title")
    private String competitionTitle;

    @Column(name = "dames_url")
    private String damesUrl;

    @Column(name = "messieurs_url")
    private String messieursUrl;

    @Column(name = "mixte_url")
    private String mixteUrl;
}