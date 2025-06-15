package ru.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.event_service.dto.NewEventDto;
import ru.practicum.event_service.model.EventState;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.user_service.dto.UserDto;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public Event save(NewEventDto dto, UserDto userDto) {
        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategory()));

        Location location = new Location();
        location.setLat(dto.getLocation().getLat());
        location.setLon(dto.getLocation().getLon());
        location = locationRepository.save(location);

        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setCategory(category);
        event.setLocation(location);
        event.setPaid(dto.getPaid() != null ? dto.getPaid() : false);
        event.setParticipantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : 0);
        event.setRequestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : true);
        event.setInitiator(userDto.getId());
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        return eventRepository.save(event);
    }
}
