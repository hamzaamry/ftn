package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    // Find rankings by competition title
    List<Ranking> findByCompetitionTitle(String competitionTitle);

    // Find rankings by event name
    List<Ranking> findByEventName(String eventName);

    // Find rankings by category (dames/messieurs)
    List<Ranking> findByCategory(String category);

    // Find rankings by club
    List<Ranking> findByClub(String club);

    // Find rankings by nation
    List<Ranking> findByNation(String nation);

    // Find rankings by birth year
    List<Ranking> findByBirthYear(String birthYear);

    // Custom query to find top N rankings by points in a specific event
    @Query("SELECT r FROM Ranking r WHERE r.eventName = ?1 ORDER BY CAST(r.points AS integer) DESC")
    List<Ranking> findTopByEventOrderByPointsDesc(String eventName, int limit);

    // Custom query to search by swimmer name
    @Query("SELECT r FROM Ranking r WHERE LOWER(r.fullName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Ranking> findBySwimmerNameContaining(String name);

    // Find rankings by competition and category
    List<Ranking> findByCompetitionTitleAndCategory(String competitionTitle, String category);

    // Find rankings by competition, event and category
    List<Ranking> findByCompetitionTitleAndEventNameAndCategory(
            String competitionTitle,
            String eventName,
            String category
    );
}