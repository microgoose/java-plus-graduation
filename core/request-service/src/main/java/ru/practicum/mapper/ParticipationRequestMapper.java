package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.request_service.dto.ParticipationRequestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    @Mappings({
            @Mapping(source = "userId", target = "requester"),
            @Mapping(source = "eventId", target = "event")
    })
    ParticipationRequestDto toDto(ParticipationRequest request);

    List<ParticipationRequestDto> toDtoList(List<ParticipationRequest> requests);
}
