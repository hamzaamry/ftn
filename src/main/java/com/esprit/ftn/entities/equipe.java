package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "equipes")
@Data
public class equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String imagePath; // Chemin ou URL de la photo

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;
}
