package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.User;
import ru.practicum.user_service.dto.NewUserRequest;
import ru.practicum.user_service.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    User toEntity(NewUserRequest request);
}
