package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "clubs")
@Data
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "link", length = 512)
    private String link;

    @Column(name = "source_url", length = 512)
    private String sourceUrl;

    @Column(name = "discipline")
    private String discipline;
}