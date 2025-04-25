package com.esprit.ftn.Controllers;

import com.esprit.ftn.Repositories.RankingRepository;
import com.esprit.ftn.entities.Ranking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rankings")
@CrossOrigin(origins = "*")
public class RankingController {

    @Autowired
    private RankingRepository rankingRepository;

    // Get all rankings
    @GetMapping
    public List<Ranking> getAllRankings() {
        return rankingRepository.findAll();
    }

    // Get ranking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ranking> getRankingById(@PathVariable Long id) {
        Optional<Ranking> ranking = rankingRepository.findById(id);
        return ranking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new ranking
    @PostMapping
    public Ranking createRanking(@RequestBody Ranking ranking) {
        return rankingRepository.save(ranking);
    }

    // Update ranking
    @PutMapping("/{id}")
    public ResponseEntity<Ranking> updateRanking(
            @PathVariable Long id,
            @RequestBody Ranking rankingDetails) {
        return rankingRepository.findById(id)
                .map(existingRanking -> {
                    existingRanking.setCompetitionTitle(rankingDetails.getCompetitionTitle());
                    existingRanking.setCompetitionUrl(rankingDetails.getCompetitionUrl());
                    existingRanking.setEventName(rankingDetails.getEventName());
                    existingRanking.setCategory(rankingDetails.getCategory());
                    existingRanking.setCategoryUrl(rankingDetails.getCategoryUrl());
                    existingRanking.setEventTitle(rankingDetails.getEventTitle());
                    existingRanking.setPlace(rankingDetails.getPlace());
                    existingRanking.setFullName(rankingDetails.getFullName());
                    existingRanking.setNation(rankingDetails.getNation());
                    existingRanking.setBirthYear(rankingDetails.getBirthYear());
                    existingRanking.setClub(rankingDetails.getClub());
                    existingRanking.setTime(rankingDetails.getTime());
                    existingRanking.setPoints(rankingDetails.getPoints());
                    existingRanking.setPassageTime(rankingDetails.getPassageTime());
                    Ranking updatedRanking = rankingRepository.save(existingRanking);
                    return ResponseEntity.ok(updatedRanking);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete ranking
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRanking(@PathVariable Long id) {
        return rankingRepository.findById(id)
                .map(ranking -> {
                    rankingRepository.delete(ranking);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get rankings by competition
    @GetMapping("/competition/{title}")
    public List<Ranking> getRankingsByCompetition(@PathVariable String title) {
        return rankingRepository.findByCompetitionTitle(title);
    }

    // Get rankings by event
    @GetMapping("/event/{eventName}")
    public List<Ranking> getRankingsByEvent(@PathVariable String eventName) {
        return rankingRepository.findByEventName(eventName);
    }

    // Get rankings by category (dames/messieurs)
    @GetMapping("/category/{category}")
    public List<Ranking> getRankingsByCategory(@PathVariable String category) {
        return rankingRepository.findByCategory(category);
    }

    // Get rankings by swimmer name
    @GetMapping("/swimmer/{name}")
    public List<Ranking> getRankingsBySwimmerName(@PathVariable String name) {
        return rankingRepository.findBySwimmerNameContaining(name);
    }

    // Get rankings by competition and category
    @GetMapping("/competition/{title}/category/{category}")
    public List<Ranking> getRankingsByCompetitionAndCategory(
            @PathVariable String title,
            @PathVariable String category) {
        return rankingRepository.findByCompetitionTitleAndCategory(title, category);
    }

    // Get top rankings for an event
    @GetMapping("/top/{eventName}")
    public List<Ranking> getTopRankingsForEvent(
            @PathVariable String eventName,
            @RequestParam(defaultValue = "10") int limit) {
        return rankingRepository.findTopByEventOrderByPointsDesc(eventName, limit);
    }
}