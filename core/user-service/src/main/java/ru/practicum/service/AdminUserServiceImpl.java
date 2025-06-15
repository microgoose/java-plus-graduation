package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.error.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.user_service.dto.NewUserRequest;
import ru.practicum.user_service.dto.UserDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<User> users = (Objects.isNull(ids) || ids.isEmpty())
                ? userRepository.findAll(page).getContent()
                : userRepository.findByIdIn(ids, page);
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto createUser(NewUserRequest request) {
        User user = userMapper.toEntity(request);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
