package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation_service.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participation_service.dto.EventRequestStatusUpdateResult;
import ru.practicum.participation_service.dto.ParticipationRequestDto;
import ru.practicum.service.PrivateEventRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/requests")
@RequiredArgsConstructor
public class PrivateEventRequestsController {

    private final PrivateEventRequestService requestService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequests(@PathVariable Long userId,
                                                                          @PathVariable Long eventId) {
        return ResponseEntity.ok(requestService.getEventRequests(userId, eventId));
    }

    @PatchMapping
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(@PathVariable Long userId,
                                                                              @PathVariable Long eventId,
                                                                              @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        return ResponseEntity.ok(requestService.updateRequestStatus(userId, eventId, request));
    }
}
