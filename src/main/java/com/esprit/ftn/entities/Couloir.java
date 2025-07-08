package com.esprit.ftn.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Couloir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom; // par exemple "Couloir 1", "Couloir 2" etc.

    private int longueur; // si tu veux ajouter d'autres propriétés
    @ManyToOne
    @JoinColumn(name = "piscine_id")
    @JsonIgnoreProperties("couloirs") // garde celui-là
    private Piscine piscine;
}