package com.esprit.ftn.Controllers;

import com.esprit.ftn.Repositories.*;
import com.esprit.ftn.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/athlete")
@CrossOrigin(origins = "*")
public class AthleteController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private AthleteEventRepository athleteEventRepository;

    @Autowired
    private RankingRepository rankingRepository;
    @Autowired
    private CompetitionRepository competitionRepository;

    @GetMapping("/getAllAthletesWithCompetitionsAndEvents")
    public List<Athlete> getAllAthletesWithCompetitionsAndEvents() {
        List<Athlete> athletes = athleteRepository.findAll();
        for (Athlete athlete : athletes) {
            List<AthleteEvent> athletesEvent = athleteEventRepository.findByAthleteId(athlete.getId());
            for(AthleteEvent athleteEvent : athletesEvent) {
                var competitions = competitionRepository.findByTitleContaining(athleteEvent.getEvent().getCompetitionTitle());
                for (Competition competition : competitions) {
                    List<Event> events =
                            eventRepository.findByCompetitionTitle(competition.getTitle());
                    for (Event event : events) {
                        List<Ranking> rankings = rankingRepository.findByEventName(event.getEventTitle());
                        event.setRankings(rankings);
                    }
                    competition.setEvents(events);
                }
                athlete.setCompetitions(competitions);
            }
        }
        return athletes;
    }

    @GetMapping("/getAthletesWithCompetitionsAndEvents/{id}")
    public ResponseEntity<Athlete> getAthletesWithCompetitionsAndEvents(@PathVariable Long id) {
        if (!athleteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Athlete athlete = athleteRepository.findById(id).get();

        List<AthleteEvent> athletesEvent = athleteEventRepository.findByAthleteId(id);
        for(AthleteEvent athleteEvent : athletesEvent) {
            var competitions = competitionRepository.findByTitleContaining(athleteEvent.getEvent().getCompetitionTitle());
            for (Competition competition : competitions) {
            List<Event> events =
                    eventRepository.findByCompetitionTitle(competition.getTitle());
            for (Event event : events) {
                List<Ranking> rankings = rankingRepository.findByEventName(event.getEventTitle());
                event.setRankings(rankings);
            }
            competition.setEvents(events);
        }
            athlete.setCompetitions(competitions);
        }
        return ResponseEntity.ok(athlete);
    }

    @PostMapping("/signup")
    public ResponseEntity<AthleteEvent> signup(@RequestParam Long athleteId,
                                               @RequestParam Long eventId) {
        Athlete athlete = athleteRepository.findById(athleteId).orElse(null);
        Event   event   = eventRepository.findById(eventId).orElse(null);
        if (athlete == null || event == null) {
            return ResponseEntity.badRequest().build();
        }
        if(athleteEventRepository.findByEventIdAndAthleteId(eventId,athleteId) != null) {
            return ResponseEntity.badRequest().build();
        }
        AthleteEvent ae = new AthleteEvent();
        ae.setAthlete(athlete);
        ae.setEvent(event);
        ae.setConfirmed(null);
        return ResponseEntity.ok(athleteEventRepository.save(ae));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<AthleteEvent> confirm(@PathVariable Long id) {
        return athleteEventRepository.findById(id)
                .map(ae -> {
                    ae.setConfirmed(true);
                    return ResponseEntity.ok(athleteEventRepository.save(ae));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/confirm/{eventid}/{athletId}/{response}")
    public ResponseEntity<AthleteEvent> confirm(@PathVariable Long eventid,@PathVariable Long athletId,@PathVariable String response) {

        var ae= athleteEventRepository.findByEventIdAndAthleteId(eventid,athletId);
        if (response == "null") {
            ae.setConfirmed(null);

        }
        else ae.setConfirmed(Boolean.parseBoolean(response));
        return ResponseEntity.ok(athleteEventRepository.save(ae));
    }

    @GetMapping("/check/{athleteId}/{eventId}")
    public boolean check(@PathVariable Long athleteId,@PathVariable Long eventId) {
        return athleteEventRepository.findByEventIdAndAthleteId(eventId,athleteId).isConfirmed();
    }

    @GetMapping("/by-athlete/{athleteId}")
    public List<AthleteEvent> byAthlete(@PathVariable Long athleteId) {
        return athleteEventRepository.findByAthleteId(athleteId);
    }

    @GetMapping("/by-event/{eventId}")
    public List<AthleteEvent> byEvent(@PathVariable Long eventId) {
        return athleteEventRepository.findByEventId(eventId);
    }
}