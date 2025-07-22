package com.cognyte.event_manager.backend.controller;

import com.cognyte.event_manager.backend.domain.Event;
import com.cognyte.event_manager.backend.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public EventService eventService() {
            return Mockito.mock(EventService.class);
        }
    }

    @Test
    void testCreateEvent_returnsCreatedEvent() throws Exception {
        Event eventToCreate = new Event();
        eventToCreate.setTitle("Test Event");
        eventToCreate.setStartDate(Instant.now());
        eventToCreate.setEndDate(Instant.now().plusSeconds(3600));
        eventToCreate.setStatus(com.cognyte.event_manager.backend.domain.enumeration.EventStatus.STARTED);

        Event createdEvent = new Event();
        createdEvent.setId(1L);
        createdEvent.setTitle(eventToCreate.getTitle());
        createdEvent.setStartDate(eventToCreate.getStartDate());
        createdEvent.setEndDate(eventToCreate.getEndDate());
        createdEvent.setStatus(eventToCreate.getStatus());

        given(eventService.save(any(Event.class))).willReturn(createdEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void testGetEvent_returnsEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");

        given(eventService.findById(1L)).willReturn(Optional.of(event));

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void testGetEvent_notFound() throws Exception {
        given(eventService.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/events/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllEvents_returnsPagedEvents() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");

        Page<Event> page = new PageImpl<>(List.of(event));

        given(eventService.findAll(any(PageRequest.class))).willReturn(page);

        mockMvc.perform(get("/api/events?page=1&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.current").value(1));
    }

    @Test
    void testDeleteEvent_returnsNoContent() throws Exception {
        Event event = new Event();
        event.setId(1L);

        given(eventService.findById(1L)).willReturn(Optional.of(event));
        Mockito.doNothing().when(eventService).delete(1L);

        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteEvent_notFound() throws Exception {
        given(eventService.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/events/999"))
                .andExpect(status().isNotFound());
    }
}
