package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event_service.model.EventState;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findByIdIn(Collection<Long> id);

    List<Event> findByInitiator(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiator(Long eventId, Long userId);

    @Query("SELECT e FROM Event e " +
           "WHERE (:users IS NULL OR e.initiator IN :users) " +
           "AND (:states IS NULL OR e.state IN :states) " +
           "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:start IS NULL OR e.eventDate >= :start) " +
            "AND (:end IS NULL OR e.eventDate < :end) ")
    List<Event> findAdminFiltered(@Param("users") List<Long> users,
                                  @Param("states") List<EventState> states,
                                  @Param("categories") List<Long> categories,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end,
                                  Pageable pageable);

    Optional<Event> findByIdAndState(Long id, EventState state);
}
