package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.controller.RecommendationsClient;
import ru.practicum.controller.UserActionClient;
import ru.practicum.error.NotFoundException;
import ru.practicum.event_service.dto.*;
import ru.practicum.event_service.model.EventState;
import ru.practicum.ewm.stats.proto.ActionTypeProto;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.EventSpecification;
import ru.practicum.request_service.client.AdminRequestsClient;
import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.request_service.dto.RequestSearchDto;
import ru.practicum.request_service.model.ParticipationRequestStatus;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final EventMappingService eventMapper;
    private final AdminRequestsClient adminRequestsClient;
    private final RecommendationsClient recommendationsClient;
    private final UserActionClient collectorClient;

    @Override
    public List<EventShortDto> getEvents(SearchEventsDto filter, LookEventDto lookEventDto) {
        List<Event> events = eventRepository.findAll(EventSpecification.withFilters(filter));

        if (Objects.equals(filter.getOnlyAvailable(), true)) {
            List<Long> eventsIds = events.stream()
                    .map(Event::getId)
                    .toList();

            List<ParticipationRequestDto> participationRequestDto = adminRequestsClient
                    .searchRequests(RequestSearchDto.builder()
                            .eventIds(eventsIds)
                            .status(ParticipationRequestStatus.CONFIRMED)
                            .build());

            List<Long> availableEventIds = participationRequestDto.stream()
                    .map(ParticipationRequestDto::getEvent)
                    .toList();

            events = events.stream()
                    .filter(event -> availableEventIds.contains(event.getId()))
                    .toList();
        }

        List<EventShortDto> eventShortDtoList = eventMapper.toShortDtoList(events);

        List<Long> eventIds = eventShortDtoList.stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());

        Map<Long, Double> ratings = getEventRatings(eventIds);
        eventShortDtoList.forEach(event -> event.setRating(ratings.getOrDefault(event.getId(), 0.0)));

        if (filter.getSort() == null || EventSort.EVENT_DATE.equals(filter.getSort())) {
            eventShortDtoList = eventShortDtoList.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate)) // Сортируем по eventDate
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            eventShortDtoList = eventShortDtoList.stream()
                    .sorted(Comparator.comparingDouble(EventShortDto::getRating).reversed()) // Сортируем по rating
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        int from = filter.getFrom();
        int size = filter.getSize();

        if (from < 0 || size <= 0)
            throw new IllegalArgumentException("Invalid pagination parameters: " + from + ", " + size);
        if (from >= eventShortDtoList.size())
            return Collections.emptyList();

        return eventShortDtoList.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId, Long userId) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Published event not found with id: " + eventId));

        EventFullDto eventFullDto = eventMapper.toFullDto(event);
        eventFullDto.setRating(getEventRating(eventId));
        collectorClient.sendUserAction(userId, eventId, ActionTypeProto.ACTION_VIEW, Instant.now());

        return eventFullDto;
    }

    private double getEventRating(Long eventId) {
        Stream<RecommendedEventProto> interactions = recommendationsClient.getInteractionsCount(List.of(eventId));
        return interactions
                .filter(proto -> proto.getEventId() == eventId)
                .findFirst()
                .map(RecommendedEventProto::getScore)
                .orElse(0.0);
    }

    private Map<Long, Double> getEventRatings(List<Long> eventIds) {
        Stream<RecommendedEventProto> interactions = recommendationsClient.getInteractionsCount(eventIds);
        return interactions
                .collect(Collectors.toMap(
                        RecommendedEventProto::getEventId,
                        RecommendedEventProto::getScore,
                        (existing, replacement) -> existing));
    }
}
