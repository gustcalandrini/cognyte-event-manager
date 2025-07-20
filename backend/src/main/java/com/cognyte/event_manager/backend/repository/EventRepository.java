package com.cognyte.event_manager.backend.repository;

import com.cognyte.event_manager.backend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
