package com.esprit.ftn.Controllers;

import com.esprit.ftn.Repositories.EventRepository;
import com.esprit.ftn.entities.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/competition/{competitionTitle}")
    public List<Event> getEventsByCompetitionTitle(@PathVariable String competitionTitle) {
        return eventRepository.findByCompetitionTitle(competitionTitle);
    }

    @GetMapping("/search")
    public List<Event> searchEventsByTitle(@RequestParam String title) {
        return eventRepository.findByEventTitleContaining(title);
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOptional.get();
        event.setEventTitle(eventDetails.getEventTitle());
        event.setCompetitionTitle(eventDetails.getCompetitionTitle());
        event.setDamesUrl(eventDetails.getDamesUrl());
        event.setMessieursUrl(eventDetails.getMessieursUrl());
        event.setMixteUrl(eventDetails.getMixteUrl());

        Event updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}