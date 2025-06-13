package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.event_service.dto.LocationDto;
import ru.practicum.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toDto(Location location);

    Location toEntity(LocationDto locationDto);
}
