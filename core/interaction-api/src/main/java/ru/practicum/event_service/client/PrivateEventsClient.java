package ru.practicum.event_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;
import ru.practicum.event_service.dto.NewEventDto;
import ru.practicum.event_service.dto.UpdateEventUserRequest;

import java.util.List;

@FeignClient(name = "event-service", contextId = "PrivateEventsClient")
public interface PrivateEventsClient {

    @GetMapping("/users/{userId}/events")
    List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size);

    @PostMapping("/users/{userId}/events")
    EventFullDto create(@PathVariable Long userId, @RequestBody NewEventDto dto);

    @GetMapping("/users/{userId}/events/{eventId}")
    EventFullDto getById(@PathVariable Long userId, @PathVariable Long eventId);

    @PatchMapping("/users/{userId}/events/{eventId}")
    EventFullDto update(@PathVariable Long userId,
                        @PathVariable Long eventId,
                        @RequestBody UpdateEventUserRequest request);
}
