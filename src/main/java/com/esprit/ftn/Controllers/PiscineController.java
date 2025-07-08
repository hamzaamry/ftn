package com.esprit.ftn.Controllers;
import com.esprit.ftn.entities.Piscine;
import com.esprit.ftn.services.PiscineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/piscines")
@RequiredArgsConstructor
public class PiscineController {
    private final PiscineService piscineService;

    @GetMapping
    public List<Piscine> getAllPiscines() {
        return piscineService.getAllPiscines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Piscine> getPiscineById(@PathVariable Long id) {
        Piscine piscine = piscineService.getPiscineById(id);
        return ResponseEntity.ok(piscine);
    }

    @PostMapping
    public ResponseEntity<Piscine> createPiscine(@RequestBody Piscine piscine) {
        Piscine createdPiscine = piscineService.createPiscine(piscine);
        return ResponseEntity.ok(createdPiscine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Piscine> updatePiscine(@PathVariable Long id, @RequestBody Piscine piscineDetails) {
        Piscine updatedPiscine = piscineService.updatePiscine(id, piscineDetails);
        return ResponseEntity.ok(updatedPiscine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePiscine(@PathVariable Long id) {
        piscineService.deletePiscine(id);
        return ResponseEntity.noContent().build();
    }
}
