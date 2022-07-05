package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto saveUser(UserDto userDto);
    UserDto updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);
}
