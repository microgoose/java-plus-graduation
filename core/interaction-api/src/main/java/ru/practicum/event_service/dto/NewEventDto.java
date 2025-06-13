package ru.practicum.event_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    private Long category;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    private Boolean paid;

    @Min(0)
    private Integer participantLimit;

    private Boolean requestModeration;
}
