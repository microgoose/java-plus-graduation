package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.request_service.dto.RequestSearchDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestsServiceImpl implements ParticipationRequestsService {

    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> searchRequests(RequestSearchDto filter) {
        List<ParticipationRequest> requests;

        if (filter.getEventIds() != null && filter.getStatus() != null) {
            requests = requestRepository.findAllByEventIdInAndStatus(
                    filter.getEventIds(), filter.getStatus());
        } else if (filter.getEventIds() != null) {
            requests = requestRepository.findAllByEventIdIn(filter.getEventIds());
        } else if (filter.getStatus() != null) {
            requests = requestRepository.findAllByStatus(filter.getStatus());
        } else {
            requests = requestRepository.findAll();
        }

        return requestMapper.toDtoList(requests);
    }

    @Override
    public Long countRequests(Long eventId) {
        return requestRepository.countByEventId(eventId);
    }
}
