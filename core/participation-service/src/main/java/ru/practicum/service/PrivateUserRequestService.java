package ru.practicum.service;

import ru.practicum.participation_service.dto.ParticipationRequestDto;

import java.util.List;
public interface PrivateUserRequestService {
    List<ParticipationRequestDto> getUserRequests(Long userId);
    ParticipationRequestDto createRequest(Long userId, Long eventId);
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}