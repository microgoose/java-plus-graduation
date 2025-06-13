package ru.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.ForbiddenActionException;
import ru.practicum.error.NotFoundException;
import ru.practicum.event_service.client.PublicEventsClient;
import ru.practicum.event_service.dto.EventFullDto;
import ru.practicum.event_service.model.EventState;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.request_service.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request_service.dto.EventRequestStatusUpdateResult;
import ru.practicum.request_service.dto.ParticipationRequestDto;
import ru.practicum.request_service.model.ParticipationRequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateEventRequestServiceImpl implements PrivateEventRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final PublicEventsClient eventsClient;
    private final ParticipationRequestMapper requestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        EventFullDto event = getEventOrThrow(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenActionException("Only initiator can view requests");
        }

        return requestMapper.toDtoList(requestRepository.findAllByEventId(eventId));
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        EventFullDto event = getEventOrThrow(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenActionException("Only initiator can change request statuses");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ForbiddenActionException("Moderation is not required or participant limit is zero");
        }

        List<ParticipationRequest> requests = requestRepository.findAllById(updateRequest.getRequestIds());
        long confirmedCount = requestRepository.countByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
        long available = event.getParticipantLimit() - confirmedCount;

        if (available == 0) {
            throw new ForbiddenActionException("The number of applications has reached the limit");
        }

        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new ForbiddenActionException("Can only confirm/reject PENDING requests");
            }

            if (updateRequest.getStatus() == ParticipationRequestStatus.CONFIRMED && available > 0) {
                request.setStatus(ParticipationRequestStatus.CONFIRMED);
                confirmed.add(requestMapper.toDto(request));
                available--;
            } else {
                request.setStatus(ParticipationRequestStatus.REJECTED);
                rejected.add(requestMapper.toDto(request));
            }
        }

        requestRepository.saveAll(requests);
        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return requestMapper.toDtoList(requestRepository.findAllByUserId(userId));
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        EventFullDto event = getEventOrThrow(eventId);

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenActionException("Initiator cannot request participation in their own event");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenActionException("Cannot request participation in an unpublished event");
        }

        long confirmed = requestRepository.countByEventIdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
        if (event.getParticipantLimit() > 0 && confirmed >= event.getParticipantLimit()) {
            throw new ForbiddenActionException("Participant limit has been reached");
        }

        Optional<ParticipationRequest> existing = requestRepository.findByUserIdAndEventId(userId, eventId);
        if (existing.isPresent()) {
            throw new ForbiddenActionException("Request already exists");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setUserId(userId);
        request.setEventId(eventId);
        request.setStatus(event.getParticipantLimit() == 0 || !event.getRequestModeration() ?
                ParticipationRequestStatus.CONFIRMED : ParticipationRequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());

        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        if (!request.getUserId().equals(userId)) {
            throw new ForbiddenActionException("Only the requester can cancel the request");
        }

        request.setStatus(ParticipationRequestStatus.CANCELED);
        return requestMapper.toDto(requestRepository.save(request));
    }

    private EventFullDto getEventOrThrow(Long eventId) {
        try {
            return eventsClient.getById(eventId);
        } catch (FeignException ex) {
            if (ex.status() == 404) {
                throw new ForbiddenActionException("Event not found with id " + eventId);
            } else {
                throw ex;
            }
        }
    }
}
