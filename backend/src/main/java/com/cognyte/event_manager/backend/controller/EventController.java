package com.cognyte.event_manager.backend.controller;

import com.cognyte.event_manager.backend.domain.Event;
import com.cognyte.event_manager.backend.service.EventService;
import com.cognyte.event_manager.backend.service.dto.EventUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("")
    public ResponseEntity<Event> createEvent(@RequestBody @Valid Event event) throws URISyntaxException {
        Event createdEvent = eventService.save(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventUpdateDTO eventUpdateDTO) {
        return eventService.update(id, eventUpdateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by("startDate").descending());
        Page<Event> eventPage = eventService.findAll(pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("data", eventPage.getContent());
        response.put("total", eventPage.getTotalElements());
        response.put("current", page);

        return ResponseEntity.ok(response);
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
