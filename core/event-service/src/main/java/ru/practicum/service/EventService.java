package ru.practicum.service;

import ru.practicum.event_service.dto.NewEventDto;
import ru.practicum.model.Event;
import ru.practicum.user_service.dto.UserDto;

public interface EventService {

    Event save(NewEventDto dto, UserDto userDto);
}
