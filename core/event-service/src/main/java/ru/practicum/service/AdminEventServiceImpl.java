package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.ForbiddenActionException;
import ru.practicum.error.NotFoundException;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.SearchEventsDto;
import ru.practicum.event_service.dto.UpdateEventAdminRequest;
import ru.practicum.event_service.model.EventState;
import ru.practicum.event_service.model.EventStateAction;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.EventSpecification;
import ru.practicum.repository.LocationRepository;
import ru.practicum.request_service.client.AdminRequestsClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminEventServiceImpl implements AdminEventService {

    private final AdminRequestsClient adminRequestsClient;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventMappingService eventMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEvents(SearchEventsDto filter) {
        List<Event> events = eventRepository.findAll(EventSpecification.withFilters(filter));
        List<EventFullDto> eventsPage = events.stream()
                .skip(filter.getFrom())
                .limit(filter.getSize())
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());

        eventsPage.forEach(event -> {
            event.setConfirmedRequests(adminRequestsClient.countRequests(event.getId()));
        });

        return eventsPage;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));

        // Обновляем поля
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getAnnotation() != null) event.setAnnotation(request.getAnnotation());
        if (request.getDescription() != null) event.setDescription(request.getDescription());

        if (request.getEventDate() != null) {
            if (LocalDateTime.now().isAfter(request.getEventDate())) {
                throw new IllegalArgumentException("Event date is after event date");
            }

            event.setEventDate(request.getEventDate());
        }

        if (request.getPaid() != null) event.setPaid(request.getPaid());
        if (request.getParticipantLimit() != null) event.setParticipantLimit(request.getParticipantLimit());
        if (request.getRequestModeration() != null) event.setRequestModeration(request.getRequestModeration());

        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + request.getCategory()));
            event.setCategory(category);
        }

        if (request.getLocation() != null) {
            Location location = new Location();
            location.setLat(request.getLocation().getLat());
            location.setLon(request.getLocation().getLon());
            location = locationRepository.save(location);
            event.setLocation(location);
        }

        if (request.getStateAction() != null) {
            if (request.getStateAction() == EventStateAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new ForbiddenActionException("Only pending events can be published");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (request.getStateAction() == EventStateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ForbiddenActionException("Cannot reject a published event");
                }
                event.setState(EventState.CANCELED);
            }
        }

        return eventMapper.toFullDto(eventRepository.save(event));
    }
}
