package ru.practicum.event_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.dto.UpdateEventAdminRequest;

import java.util.List;

@FeignClient(name = "event-service", contextId = "AdminEventsClient")
public interface AdminEventsClient {

    @GetMapping("/admin/events")
    List<EventFullDto> search(@RequestParam(required = false) List<Long> users,
                              @RequestParam(required = false) List<String> states,
                              @RequestParam(required = false) List<Long> categories,
                              @RequestParam(required = false) String rangeStart,
                              @RequestParam(required = false) String rangeEnd,
                              @RequestParam(defaultValue = "0") int from,
                              @RequestParam(defaultValue = "10") int size);

    @PatchMapping("/admin/events/{eventId}")
    EventFullDto update(@PathVariable Long eventId, @RequestBody UpdateEventAdminRequest request);
}
