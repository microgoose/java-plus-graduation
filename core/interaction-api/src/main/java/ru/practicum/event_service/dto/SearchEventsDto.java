package ru.practicum.event_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event_service.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchEventsDto {

    private String text;

    private List<Long> categories;

    private List<Long> users;

    private List<EventState> states;

    private Boolean paid;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Boolean onlyAvailable;

    private EventSort sort;

    private int from;

    private int size;

}