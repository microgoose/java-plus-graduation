package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.ForbiddenActionException;
import ru.practicum.error.NotFoundException;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;
import ru.practicum.event_service.dto.NewEventDto;
import ru.practicum.event_service.dto.UpdateEventUserRequest;
import ru.practicum.event_service.model.EventState;
import ru.practicum.event_service.model.EventStateAction;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.user_service.client.AdminUsersClient;
import ru.practicum.user_service.dto.UserDto;
import ru.practicum.user_service.dto.UserShortDto;
import ru.practicum.user_service.mapper.UserDtoMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateEventServiceImpl implements PrivateEventService {

    private final AdminUsersClient adminUsersClient;
    private final UserDtoMapper userDtoMapper;

    private final CategoryRepository categoryRepository;

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final EventService eventService;

    private final LocationMapper locationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        return eventMapper.toShortDtoList(
            eventRepository.findByInitiator(userId, PageRequest.of(from / size, size))
        );
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto dto) {
        UserDto userDto = getUserById(userId);
        UserShortDto userShortDto = userDtoMapper.toShortDto(userDto);
        return eventMapper.toFullDto(eventService.save(dto, userDto), userShortDto, 0L, 0L);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        UserShortDto userDto = userDtoMapper.toShortDto(getUserById(userId));
        Event event = eventRepository.findByIdAndInitiator(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found for userId=" + userId + ", eventId=" + eventId));
        return eventMapper.toFullDto(event, userDto, null, null);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));

        UserShortDto userDto = userDtoMapper.toShortDto(getUserById(userId));

        if (!Objects.equals(event.getInitiator(), userId))
            throw new ForbiddenActionException("User is not the event creator");
        if (Objects.equals(event.getState(), EventState.PUBLISHED))
            throw new ForbiddenActionException("Changing of published event is forbidden.");

        Optional.ofNullable(request.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(request.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(request.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(request.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(request.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(request.getLocation())
                .ifPresent(l -> event.setLocation(locationMapper.toEntity(l)));

        if (Objects.nonNull(request.getCategory())) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + request.getCategory()));
            event.setCategory(category);
        }

        if (Objects.nonNull(request.getStateAction())) {
            if (EventStateAction.PUBLISH_EVENT.equals(request.getStateAction())) {
                throw new ForbiddenActionException("Publishing this event is forbidden.");
            }

            switch (request.getStateAction()) {
                case EventStateAction.CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case EventStateAction.SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid state action: " + request.getStateAction());
            }
        }

        event.setRequestModeration(request.getRequestModeration());
        event.setInitiator(userId);

        return eventMapper.toFullDto(eventRepository.save(event), userDto, null, null);
    }

    private UserDto getUserById(Long userId) {
        try {
            return adminUsersClient.getUser(userId);
        } catch (Exception e) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }
}
