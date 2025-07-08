package com.esprit.ftn.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Piscine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String adresse;

    private int nombreCouloirs;

    private boolean disponible;
    // Association OneToMany entre Piscine et Couloirs
    @OneToMany(mappedBy = "piscine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("piscine") // <== Ajouté ici, et on supprime @JsonManagedReference
    private List<Couloir> couloirs;

}
