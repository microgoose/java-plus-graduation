package ru.practicum.service;

import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;
import ru.practicum.model.Event;

import java.util.List;

public interface EventMappingService {

    EventFullDto toFullDto(Event event);

    List<EventFullDto> toFullDtoList(List<Event> events);

    EventShortDto toShortDto(Event event);

    List<EventShortDto> toShortDtoList(List<Event> events);
}
