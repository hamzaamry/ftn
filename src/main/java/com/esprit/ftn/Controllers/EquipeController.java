package com.esprit.ftn.Controllers;

import com.esprit.ftn.entities.Coach;
import com.esprit.ftn.entities.equipe;
import com.esprit.ftn.Repositories.CoachRepository;
import com.esprit.ftn.Repositories.EquipeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeRepository equipeRepository;
    private final CoachRepository coachRepository;

    public EquipeController(EquipeRepository equipeRepository, CoachRepository coachRepository) {
        this.equipeRepository = equipeRepository;
        this.coachRepository = coachRepository;
    }

    @PostMapping
    public ResponseEntity<?> createEquipe(@RequestBody equipe equipe) {
        Coach coach = null;

        if (equipe.getCoach() == null) {
            return ResponseEntity.badRequest().body("Coach is required");
        }

        if (equipe.getCoach().getId() != null) {
            coach = coachRepository.findById(equipe.getCoach().getId()).orElse(null);
            if (coach == null) {
                return ResponseEntity.badRequest().body("Coach not found by id");
            }
        } else if (equipe.getCoach().getUser() != null && equipe.getCoach().getUser().getEmail() != null) {
            coach = coachRepository.findByUserEmail(equipe.getCoach().getUser().getEmail());
            if (coach == null) {
                return ResponseEntity.badRequest().body("Coach not found by email");
            }
        } else {
            return ResponseEntity.badRequest().body("Coach id or email is required");
        }

        equipe.setCoach(coach);
        equipe savedEquipe = equipeRepository.save(equipe);
        return ResponseEntity.ok(savedEquipe);
    }

    @GetMapping
    public ResponseEntity<?> getAllEquipes() {
        return ResponseEntity.ok(equipeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipeById(@PathVariable Long id) {
        return equipeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEquipe(@PathVariable Long id, @RequestBody equipe updatedEquipe) {
        return equipeRepository.findById(id)
                .map(existingEquipe -> {
                    existingEquipe.setNom(updatedEquipe.getNom());
                    existingEquipe.setImagePath(updatedEquipe.getImagePath());

                    Coach coach = null;
                    if (updatedEquipe.getCoach() != null) {
                        if (updatedEquipe.getCoach().getId() != null) {
                            coach = coachRepository.findById(updatedEquipe.getCoach().getId()).orElse(null);
                            if (coach == null) {
                                return ResponseEntity.badRequest().body("Coach not found by id");
                            }
                        } else if (updatedEquipe.getCoach().getUser() != null && updatedEquipe.getCoach().getUser().getEmail() != null) {
                            coach = coachRepository.findByUserEmail(updatedEquipe.getCoach().getUser().getEmail());
                            if (coach == null) {
                                return ResponseEntity.badRequest().body("Coach not found by email");
                            }
                        } else {
                            return ResponseEntity.badRequest().body("Coach id or email is required");
                        }
                        existingEquipe.setCoach(coach);
                    }

                    equipeRepository.save(existingEquipe);
                    return ResponseEntity.ok(existingEquipe);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipe(@PathVariable Long id) {
        return equipeRepository.findById(id)
                .map(equipe -> {
                    equipeRepository.delete(equipe);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Nouvel endpoint GET pour renvoyer tous les coachs
    @GetMapping("/coachs")
    public ResponseEntity<List<Coach>> getAllCoachs() {
        List<Coach> coachs = coachRepository.findAll();
        return ResponseEntity.ok(coachs);
    }
}
