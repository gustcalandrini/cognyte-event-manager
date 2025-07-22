package com.cognyte.event_manager.backend.service;

import com.cognyte.event_manager.backend.domain.Event;
import com.cognyte.event_manager.backend.domain.enumeration.EventStatus;
import com.cognyte.event_manager.backend.errors.BadRequestAlertException;
import com.cognyte.event_manager.backend.repository.EventRepository;
import com.cognyte.event_manager.backend.service.dto.EventUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(rollbackFor = IOException.class)
public class EventService {

    private static final String ENTITY_NAME = "event";

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event save(Event event) {
        if(event.getEndDate().isBefore(event.getStartDate())) {
            throw new BadRequestAlertException("Invalid Date", ENTITY_NAME, "End date must be after start date");
        }

        return eventRepository.save(event);
    }

    public Optional<Event> update(Long id, EventUpdateDTO eventUpdateDTO) {

        if(eventUpdateDTO.id() == null){
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "ID should not be null");
        }

        if(!Objects.equals(id, eventUpdateDTO.id())){
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "Provided ids doesn't match");
        }

        if(eventUpdateDTO.endDate().isBefore(eventUpdateDTO.startDate())) {
            throw new BadRequestAlertException("Invalid Date", ENTITY_NAME, "End date must be after start date");
        }

        return eventRepository.findById(id).map(existing -> {
            existing.setTitle(eventUpdateDTO.title());
            existing.setPrice(new BigDecimal(eventUpdateDTO.price()));
            existing.setStartDate(eventUpdateDTO.startDate());
            existing.setEndDate(eventUpdateDTO.endDate());
            existing.setStatus(EventStatus.valueOf(eventUpdateDTO.status()));
            return eventRepository.save(existing);
        });
    }


    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Optional<Event> findById(Long id){
        return eventRepository.findById(id);
    }

    public void delete(Long id){
        eventRepository.deleteById(id);
    }


}
