package ru.practicum.event_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.EventShortDto;

import java.util.List;

@FeignClient(name = "${event-service.name}", url = "${event-service.url}", contextId = "PublicEventsClient")
public interface PublicEventsClient {

    @GetMapping("/events")
    List<EventShortDto> getAll(@RequestParam(required = false) String text,
                               @RequestParam(required = false) List<Long> categories,
                               @RequestParam(required = false) Boolean paid,
                               @RequestParam(required = false) String rangeStart,
                               @RequestParam(required = false) String rangeEnd,
                               @RequestParam(required = false) Boolean onlyAvailable,
                               @RequestParam(required = false) String sort,
                               @RequestParam(defaultValue = "0") int from,
                               @RequestParam(defaultValue = "10") int size);

    @GetMapping("/events/{id}")
    EventFullDto getById(@PathVariable Long id);
}
