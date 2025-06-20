package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request_service.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request_service.dto.EventRequestStatusUpdateResult;
import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.service.PrivateEventRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestsController {

    private final PrivateEventRequestService requestService;

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequests(@PathVariable Long userId,
                                                                          @PathVariable Long eventId) {
        log.info("Getting event requests for user {} and event {}", userId, eventId);
        return ResponseEntity.ok(requestService.getEventRequests(userId, eventId));
    }

    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(@PathVariable Long userId,
                                                                              @PathVariable Long eventId,
                                                                              @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        log.info("Changing event request status for user {} and event {} to {}", userId, eventId, request.getStatus());
        return ResponseEntity.ok(requestService.updateRequestStatus(userId, eventId, request));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(@PathVariable Long userId) {
        log.info("Get user requests {}", userId);
        return ResponseEntity.ok(requestService.getUserRequests(userId));
    }

    @PostMapping("/requests")
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(@PathVariable Long userId,
                                                                           @RequestParam Long eventId) {
        log.info("Add participation request {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestService.createRequest(userId, eventId));
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable Long userId,
                                                                 @PathVariable Long requestId) {
        log.info("Cancel request {}", userId);
        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }

    @GetMapping("/check-participation/{eventId}")
    ResponseEntity<Boolean> isUserParticipatedInEvent(@PathVariable("eventId") Long eventId,
                                      @PathVariable("userId") Long userId) {

        log.info("Checking if participation {} is participated in event {}", eventId, userId);
        return ResponseEntity.ok(requestService.isUserParticipatedInEvent(eventId, userId));
    }
}
