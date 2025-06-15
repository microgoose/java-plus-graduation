package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.config.ServiceInfo;
import ru.practicum.controller.StatsClient;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.ManyEndPointDto;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.error.NotFoundException;
import ru.practicum.event_service.dto.*;
import ru.practicum.event_service.model.EventState;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.EventSpecification;
import ru.practicum.request_service.client.AdminRequestsClient;
import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.request_service.dto.RequestSearchDto;
import ru.practicum.request_service.model.ParticipationRequestStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final EventMappingService eventMapper;
    private final AdminRequestsClient adminRequestsClient;
    private final StatsClient statsClient;
    private final ServiceInfo serviceInfo;

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

        if (filter.getSort() == null || EventSort.EVENT_DATE.equals(filter.getSort())) {
            eventShortDtoList = eventShortDtoList.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate)) // Сортируем по eventDate
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            eventShortDtoList = eventShortDtoList.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews).reversed()) // Сортируем по views
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        int from = filter.getFrom();
        int size = filter.getSize();

        if (from < 0 || size <= 0)
            throw new IllegalArgumentException("Invalid pagination parameters: " + from + ", " + size);
        if (from >= eventShortDtoList.size())
            return Collections.emptyList();

        List<EventShortDto> eventsPage = eventShortDtoList.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());

        countViews(eventsPage, filter.getRangeStart(), filter.getRangeEnd(), true);

        List<String> uris = new ArrayList<>(eventsPage.stream().map(e -> "/events/" + e.getId()).toList());
        uris.add("/events");
        statsClient.saveHitGroup(new ManyEndPointDto(uris, lookEventDto.getIp()));

        return eventsPage;
    }

    @Override
    public EventFullDto getEventById(Long id, LookEventDto lookEventDto) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Published event not found with id: " + id));

        statsClient.saveHit(CreateEndpointHitDto.builder()
                .app(serviceInfo.getServiceName())
                .ip(lookEventDto.getIp())
                .uri(lookEventDto.getUri())
                .timestamp(LocalDateTime.now())
                .build());

        EventFullDto eventFullDto = eventMapper.toFullDto(event);
        countViews(List.of(eventFullDto), null, null, true);
        return eventFullDto;
    }

    public void countViews(List<? extends EventViews> events, LocalDateTime dateStart, LocalDateTime dateEnd, boolean unique) {
        if (events.isEmpty())
            return;
        if (Objects.isNull(dateStart))
            dateStart = LocalDateTime.of(1970, 1, 1, 1, 1, 1);
        if (Objects.isNull(dateEnd))
            dateEnd = LocalDateTime.of(3000, 1, 1, 1, 1, 1);

        List<String> uris = events.stream().map(e -> "/events/" + e.getId()).toList();
        List<ReadEndpointHitDto> hits = statsClient.getStats(dateStart, dateEnd, uris, unique);

        // Заносим значения views в список events
        Map<Long, Long> workMap = new HashMap<>();
        for (ReadEndpointHitDto r : hits) {
            String decoded = r.getUri();
            long i = Long.parseLong(decoded.substring(decoded.lastIndexOf("/") + 1));
            workMap.put(i, (long) r.getHits());
        }

        for (EventViews e : events) {
            e.setViews(workMap.getOrDefault(e.getId(), 0L));
        }
    }
}
