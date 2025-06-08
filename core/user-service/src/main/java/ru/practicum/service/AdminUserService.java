package ru.practicum.service;

import ru.practicum.user_service.dto.NewUserRequest;
import ru.practicum.user_service.dto.UserDto;

import java.util.List;
public interface AdminUserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);
    UserDto createUser(NewUserRequest request);
    void deleteUser(Long userId);
}