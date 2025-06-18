package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.user_service.client.AdminUsersClient;
import ru.practicum.user_service.dto.UserDto;
import ru.practicum.user_service.mapper.UserDtoMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventMappingServiceImpl implements EventMappingService {

    private final EventMapper eventMapper;
    private final AdminUsersClient usersClient;
    private final UserDtoMapper userDtoMapper;

    public EventFullDto toFullDto(Event event) {
        UserDto userDto = usersClient.getUser(event.getInitiator());
        EventFullDto dto = eventMapper.toFullDto(event);
        dto.setInitiator(userDtoMapper.toShortDto(userDto));
        return dto;
    }

    public List<EventFullDto> toFullDtoList(List<Event> events) {
        return events.stream()
            .map(this::toFullDto)
            .toList();
    }

    public EventShortDto toShortDto(Event event) {
        UserDto userDto = usersClient.getUser(event.getInitiator());
        EventShortDto dto = eventMapper.toShortDto(event);
        dto.setInitiator(userDtoMapper.toShortDto(userDto));
        return dto;
    }

    public List<EventShortDto> toShortDtoList(List<Event> events) {
        return events.stream()
            .map(this::toShortDto)
            .toList();
    }

}
