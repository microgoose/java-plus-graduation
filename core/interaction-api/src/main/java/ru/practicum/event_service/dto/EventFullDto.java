package ru.practicum.event_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event_service.model.EventState;
import ru.practicum.user_service.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto implements EventViews {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    @NotBlank
    private String description;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Boolean paid;

    @NotNull
    private LocalDateTime eventDate;

    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    private Integer participantLimit;

    private Boolean requestModeration;

    private EventState state;

    private Long confirmedRequests;

    private Long views;

    private LocationDto location;

    private UserShortDto initiator;
}
