package com.cognyte.event_manager.backend.service;

import com.cognyte.event_manager.backend.domain.Event;
import com.cognyte.event_manager.backend.domain.enumeration.EventStatus;
import com.cognyte.event_manager.backend.repository.EventRepository;
import com.cognyte.event_manager.backend.service.dto.EventUpdateDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(rollbackFor = IOException.class)
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event save(Event event) {
        if(event.getEndDate().isBefore(event.getStartDate())) {
            //ToDo: Create new kind of exception for this;
            throw new IllegalArgumentException("End date shouldn't be greater than the start Date");
        }
        return eventRepository.save(event);
    }

    public Optional<Event> update(Long id, EventUpdateDTO dto) {
        return eventRepository.findById(id).map(existing -> {
            existing.setTitle(dto.title());
            existing.setPrice(new BigDecimal(dto.price()));
            existing.setStartDate(dto.startDate());
            existing.setEndDate(dto.endDate());
            existing.setStatus(EventStatus.valueOf(dto.status()));
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
