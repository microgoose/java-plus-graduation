package ru.practicum.user_service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user_service.dto.UserDto;
import ru.practicum.user_service.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    UserShortDto toShortDto(UserDto user);

}
