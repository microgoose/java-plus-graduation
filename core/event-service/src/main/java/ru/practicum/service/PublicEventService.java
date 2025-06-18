package ru.practicum.service;

import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;
import ru.practicum.event_service.dto.LookEventDto;
import ru.practicum.event_service.dto.SearchEventsDto;

import java.util.List;

public interface PublicEventService {

    List<EventShortDto> getEvents(SearchEventsDto searchEventsDto, LookEventDto lookEventDto);

    EventFullDto getEventById(Long id, LookEventDto lookEventDto);
}