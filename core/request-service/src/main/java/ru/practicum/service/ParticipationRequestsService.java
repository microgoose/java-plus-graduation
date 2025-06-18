package ru.practicum.service;

import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.request_service.dto.RequestSearchDto;

import java.util.List;

public interface ParticipationRequestsService {

    List<ParticipationRequestDto> searchRequests(RequestSearchDto filter);

    Long countRequests(Long eventId);

}
