package ru.practicum.service;

import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.SearchEventsDto;
import ru.practicum.event_service.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> findEvents(SearchEventsDto filter);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request);
}