package com.esprit.ftn.Repositories;

import com.esprit.ftn.entities.AthleteEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AthleteEventRepository extends JpaRepository<AthleteEvent, Long> {
    List<AthleteEvent> findByAthleteId(Long athleteId);
    List<AthleteEvent> findByEventId(Long eventId);
    AthleteEvent findByEventIdAndAthleteId(Long eventId,Long athleteId);

}
