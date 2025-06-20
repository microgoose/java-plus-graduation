package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.request_service.model.ParticipationRequestStatus;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventIdInAndStatus(List<Long> eventIds, ParticipationRequestStatus status);

    List<ParticipationRequest> findAllByEventIdIn(List<Long> eventIds);

    List<ParticipationRequest> findAllByStatus(ParticipationRequestStatus status);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByUserId(Long userId);

    Optional<ParticipationRequest> findByUserIdAndEventId(Long userId, Long eventId);

    long countByEventIdAndStatus(Long eventId, ParticipationRequestStatus status);

    long countByEventId(Long eventId);

}
