package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;
import ru.practicum.model.Event;
import ru.practicum.user_service.dto.UserShortDto;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final LocationMapper locationMapper;
    private final CategoryMapper categoryMapper;

    public EventFullDto toFullDto(Event event, UserShortDto userShortDto, Double rating, Long confirmedRequests) {
        if (Objects.isNull(event)) {
            return null;
        }

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .paid(event.getPaid())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .confirmedRequests(confirmedRequests)
                .rating(rating)
                .location(locationMapper.toDto(event.getLocation()))
                .initiator(userShortDto)
                .category(categoryMapper.toDto(event.getCategory()))
                .build();
    }

    public EventFullDto toFullDto(Event event) {
        return toFullDto(event, null, null, null);
    }

    public List<EventFullDto> toFullDtoList(List<Event> events) {
        return events.stream()
                .map(this::toFullDto)
                .toList();
    }

    public EventShortDto toShortDto(Event event, UserShortDto userShortDto,  Double rating, Long confirmedRequests) {
        if (Objects.isNull(event)) {
            return null;
        }

        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .paid(event.getPaid())
                .eventDate(event.getEventDate())
                .confirmedRequests(confirmedRequests)
                .rating(rating)
                .initiator(userShortDto)
                .category(categoryMapper.toDto(event.getCategory()))
                .build();
    }

    public EventShortDto toShortDto(Event event) {
        return toShortDto(event, null, null, null);
    }

    public List<EventShortDto> toShortDtoList(List<Event> events) {
        return events.stream()
                .map(this::toShortDto)
                .toList();
    }
}
