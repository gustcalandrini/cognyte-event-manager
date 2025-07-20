package com.cognyte.event_manager.backend.service;

import com.cognyte.event_manager.backend.domain.Event;
import com.cognyte.event_manager.backend.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
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

    public Event update(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findAll(){
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id){
        return eventRepository.findById(id);
    }

    public void delete(Long id){
        eventRepository.deleteById(id);
    }


}
