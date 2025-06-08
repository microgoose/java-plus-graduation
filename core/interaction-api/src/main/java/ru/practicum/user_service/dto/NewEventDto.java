package ru.practicum.user_service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event_service.dto.LocationDto;

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

    @NotBlank
    private String eventDate; // формат "yyyy-MM-dd HH:mm:ss"

    @NotNull
    private LocationDto location;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;
}
