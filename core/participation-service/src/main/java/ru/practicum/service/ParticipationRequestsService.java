package ru.practicum.service;

import ru.practicum.participation_service.dto.ParticipationRequestDto;
import ru.practicum.participation_service.dto.RequestSearchDto;

import java.util.List;

public interface ParticipationRequestsService {

    List<ParticipationRequestDto> searchRequests(RequestSearchDto filter);

    Long countRequests(Long eventId);

}
