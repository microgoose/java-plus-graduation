package ru.practicum.event_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user_service.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto implements EventRating {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Boolean paid;

    @NotNull
    private LocalDateTime eventDate;

    private Long confirmedRequests;

    private Double rating;

    @NotNull
    private UserShortDto initiator;
}
