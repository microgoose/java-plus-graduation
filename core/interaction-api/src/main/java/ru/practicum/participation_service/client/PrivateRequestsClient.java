package ru.practicum.participation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation_service.dto.ParticipationRequestDto;

import java.util.List;
@FeignClient(name = "participation-service")
public interface PrivateRequestsClient {

    @GetMapping("/users/{userId}/requests")
    List<ParticipationRequestDto> getAll(@PathVariable Long userId);

    @PostMapping("/users/{userId}/requests")
    ParticipationRequestDto create(@PathVariable Long userId,
                                   @RequestParam Long eventId);

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancel(@PathVariable Long userId,
                                   @PathVariable Long requestId);
}
