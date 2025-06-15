package ru.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.event_service.model.EventState;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "is_paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "initiator_id")
    private Long initiator;

    @Column(name = "creation_date")
    private LocalDateTime createdOn;

    @Column(name = "publication_date")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    private EventState state;

}
