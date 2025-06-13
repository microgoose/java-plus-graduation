package ru.practicum.service;

import ru.practicum.request_service.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request_service.dto.EventRequestStatusUpdateResult;
import ru.practicum.request_service.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventRequestService {

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}