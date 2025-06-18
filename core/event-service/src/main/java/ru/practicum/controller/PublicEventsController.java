package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.*;
import ru.practicum.service.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventsController {

    private final PublicEventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getFilteredEvents(
            @RequestParam(required = false, defaultValue = "") String text,
            @RequestParam(required = false, defaultValue = "") List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") EventSort sort,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size,
            HttpServletRequest request) {

        categories.forEach(category -> {
            if (category < 0)
                throw new IllegalArgumentException("Invalid categories list");
        });

        LocalDateTime start = Objects.isNull(rangeStart) ? LocalDateTime.now() : rangeStart;
        LocalDateTime end = Objects.nonNull(rangeEnd) ? rangeEnd : null;

        if (Objects.nonNull(end) && !end.isAfter(start))
            throw new IllegalArgumentException("Invalid dates");

        LookEventDto lookEventDto = LookEventDto.builder()
                .id(null)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build();

        SearchEventsDto searchEventsDto = SearchEventsDto.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(start)
                .rangeEnd(end)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();

        log.info("Get filtered events {}, IP: {}", searchEventsDto, request.getRequestURI());

        List<EventShortDto> result = eventService.getEvents(searchEventsDto, lookEventDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long id, HttpServletRequest request) {
        log.info("Get event {}", id);

        return ResponseEntity.ok(eventService.getEventById(id, LookEventDto.builder()
                .id(null)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .build()));
    }
}
