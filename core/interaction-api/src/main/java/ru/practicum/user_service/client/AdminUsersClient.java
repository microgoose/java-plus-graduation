package ru.practicum.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user_service.dto.NewUserRequest;
import ru.practicum.user_service.dto.UserDto;

import java.util.List;
@FeignClient(name = "user-service")
public interface AdminUsersClient {

    @GetMapping("/admin/users")
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                           @RequestParam(defaultValue = "0") int from,
                           @RequestParam(defaultValue = "10") int size);

    @PostMapping("/admin/users")
    UserDto create(@RequestBody NewUserRequest request);

    @DeleteMapping("/admin/users/{userId}")
    void delete(@PathVariable Long userId);
}
