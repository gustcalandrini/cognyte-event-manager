package com.cognyte.event_manager.backend;

import com.cognyte.event_manager.backend.service.EventService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public EventService eventService() {
        return Mockito.mock(EventService.class);
    }

    // Add other mocks here if needed
}
