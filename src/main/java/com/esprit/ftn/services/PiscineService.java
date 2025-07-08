package com.esprit.ftn.services;

import com.esprit.ftn.Repositories.PiscineRepository;
import com.esprit.ftn.entities.Piscine;
import com.esprit.ftn.entities.Couloir;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PiscineService {
    private final PiscineRepository piscineRepository;

    public List<Piscine> getAllPiscines() {
        return piscineRepository.findAll();
    }

    public Piscine getPiscineById(Long id) {
        return piscineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piscine non trouvée avec l'ID : " + id));
    }

    public Piscine createPiscine(Piscine piscine) {
        // On s'assure que chaque couloir référence bien la piscine (relation bidirectionnelle)
        if (piscine.getCouloirs() != null) {
            for (Couloir couloir : piscine.getCouloirs()) {
                couloir.setPiscine(piscine);
            }
        }

        return piscineRepository.save(piscine);
    }

    public Piscine updatePiscine(Long id, Piscine piscineDetails) {
        Piscine piscine = getPiscineById(id);
        piscine.setNom(piscineDetails.getNom());
        piscine.setAdresse(piscineDetails.getAdresse());
        piscine.setDisponible(piscineDetails.isDisponible());

        // Mise à jour des couloirs :
        // On supprime les anciens couloirs et on ajoute les nouveaux
        if (piscineDetails.getCouloirs() != null) {
            // Détacher anciens couloirs
            piscine.getCouloirs().clear();
            // Ajouter les nouveaux couloirs
            for (Couloir couloir : piscineDetails.getCouloirs()) {
                couloir.setPiscine(piscine);
                piscine.getCouloirs().add(couloir);
            }
        }

        return piscineRepository.save(piscine);
    }

    public void deletePiscine(Long id) {
        Piscine piscine = getPiscineById(id);
        piscineRepository.delete(piscine);
    }
}
