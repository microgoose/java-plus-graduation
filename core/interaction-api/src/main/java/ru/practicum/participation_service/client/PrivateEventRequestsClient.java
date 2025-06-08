package ru.practicum.participation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.participation_service.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participation_service.dto.EventRequestStatusUpdateResult;
import ru.practicum.participation_service.dto.ParticipationRequestDto;

import java.util.List;
@FeignClient(name = "participation-service")
public interface PrivateEventRequestsClient {

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                              @PathVariable Long eventId);

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    EventRequestStatusUpdateResult changeStatus(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @RequestBody EventRequestStatusUpdateRequest request);
}
