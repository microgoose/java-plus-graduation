package ru.practicum.request_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request_service.model.ParticipationRequestStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSearchDto {
    private List<Long> eventIds;
    private ParticipationRequestStatus status;
}
