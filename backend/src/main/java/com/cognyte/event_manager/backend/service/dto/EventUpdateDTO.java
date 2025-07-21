package com.cognyte.event_manager.backend.service.dto;

import java.time.Instant;

public record EventUpdateDTO(
        Long id,
        String title,
        String price,
        String status,
        Instant startDate,
        Instant endDate
) {}

