package com.cognyte.event_manager.backend.controller;

import com.cognyte.event_manager.backend.domain.Event;
import com.cognyte.event_manager.backend.repository.EventRepository;
import com.cognyte.event_manager.backend.service.EventService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventController {

    private final EventService eventService;

    private final EventRepository eventRepository;

    public EventController(EventService eventService, EventRepository eventRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    @PostMapping("")
    public ResponseEntity<Event> createEvent(@RequestBody @Valid Event event) throws URISyntaxException {
        Event createdEvent = eventService.save(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable(value = "id", required = false) final Long id, @RequestBody Event event) throws URISyntaxException, BadRequestException {
        if (event.getId() == null) {
            throw new BadRequestException("ID null");
        }

        if (!Objects.equals(id, event.getId())) {
            throw new BadRequestException("Invalid ID");
        }

        if (!eventRepository.existsById(event.getId())) {
            throw new BadRequestException("Entity not found");
        }

        return ResponseEntity.ok(eventService.update(event));
    }

    @GetMapping("")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable(value = "id", required = false) final Long id) {
        return eventService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable(value = "id", required = true) Long id){
        if(eventService.findById(id).isPresent()){
            eventService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
