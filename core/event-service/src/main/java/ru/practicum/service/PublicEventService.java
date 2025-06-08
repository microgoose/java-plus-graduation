package ru.practicum.service;

import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;

import java.util.List;
public interface PublicEventService {
    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                  String sort, int from, int size);
    EventFullDto getEventById(Long id);
}