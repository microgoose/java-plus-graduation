package ru.practicum.service;

import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.UpdateEventAdminRequest;

import java.util.List;
public interface AdminEventService {
    List<EventFullDto> findEvents(List<Long> users, List<String> states, List<Long> categories,
                                  String rangeStart, String rangeEnd, int from, int size);
    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request);
}