package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCompetitionTitle(String competitionTitle);
    List<Event> findByEventTitleContaining(String eventTitle);
}