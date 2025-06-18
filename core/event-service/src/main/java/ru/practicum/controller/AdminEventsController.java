package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.SearchEventsDto;
import ru.practicum.event_service.dto.UpdateEventAdminRequest;
import ru.practicum.event_service.model.EventState;
import ru.practicum.service.AdminEventService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventsController {

    private final AdminEventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(@RequestParam(required = false) String users,
                                                        @RequestParam(required = false) List<String> states,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                        @RequestParam(required = false) LocalDateTime rangeStart,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                        @RequestParam(required = false) LocalDateTime rangeEnd,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {

        log.info("Get events from {} to {} ", from, size);

        List<Long> usersList = Objects.isNull(users) ? List.of() : Arrays.stream(users.split(","))
                .map(Long::valueOf)
                .toList();

        List<EventState> stateList = Objects.isNull(states) ? null : states.stream().map(EventState::valueOf).toList();

        SearchEventsDto filter = SearchEventsDto.builder()
                .users(usersList)
                .states(stateList)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return ResponseEntity.ok(eventService.findEvents(filter));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long eventId,
                                                    @RequestBody @Valid UpdateEventAdminRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, request));
    }
}
