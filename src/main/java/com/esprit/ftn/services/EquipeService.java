package com.esprit.ftn.services;


import com.esprit.ftn.entities.Coach;
import com.esprit.ftn.entities.equipe;
import com.esprit.ftn.Repositories.CoachRepository;
import com.esprit.ftn.Repositories.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
    public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private CoachRepository coachRepository;

    public List<equipe> getAllEquipes() {
        return equipeRepository.findAll();
    }

    public equipe addEquipe(equipe equipe) {
        return equipeRepository.save(equipe);
    }

    public equipe updateEquipe(Long id, equipe updatedEquipe) {
        Optional<equipe> optional = equipeRepository.findById(id);
        if (optional.isPresent()) {
            equipe existing = optional.get();
            existing.setNom(updatedEquipe.getNom());
            existing.setImagePath(updatedEquipe.getImagePath());
            existing.setCoach(updatedEquipe.getCoach());
            return equipeRepository.save(existing);
        }
        return null;
    }

    public void deleteEquipe(Long id) {
        equipeRepository.deleteById(id);
    }

    public equipe affecterCoach(Long equipeId, Long coachId) {
        equipe equipe = equipeRepository.findById(equipeId).orElse(null);
        Coach coach = coachRepository.findById(coachId).orElse(null);
        if (equipe != null && coach != null) {
            equipe.setCoach(coach);
            return equipeRepository.save(equipe);
        }
        return null;
    }
}

