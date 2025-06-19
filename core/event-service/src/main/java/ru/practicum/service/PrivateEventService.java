package ru.practicum.service;

import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;
import ru.practicum.event_service.dto.NewEventDto;
import ru.practicum.event_service.dto.UpdateEventUserRequest;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto addEvent(Long userId, NewEventDto dto);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    void likeEvent(Long userId, Long eventId);
}