package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findBySeason(String season);

    List<Competition> findByTitleContaining(String keyword);
}