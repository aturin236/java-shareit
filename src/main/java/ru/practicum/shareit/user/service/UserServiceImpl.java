package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(
                    String.format("Пользователь с id=%s не найден", userId)
            );
        }

        return user.map(UserMapper::toUserDto).orElse(null);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (userRepository.getUserByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException(
                    String.format("Пользователь с email=%s уже существует", userDto.getEmail())
            );
        }
       return UserMapper.toUserDto(
               userRepository.saveUser(
                       UserMapper.toUser(userDto)
               )
       );
    }

    @Override
    public @Valid UserDto updateUser(Long userId, UserDto userDto) {
        Optional<User> userOptional = userRepository.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(
                    String.format("Пользователь с id=%s не найден", userId)
            );
        }
        userRepository.getUserByEmail(userDto.getEmail()).ifPresent(
                x -> {
                    if (!x.getId().equals(userDto.getId())) {
                        throw new UserAlreadyExistException(
                                String.format("Пользователь с email=%s уже существует", userDto.getEmail())
                        );
                    }
                }
        );
        User user = userOptional.get();

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        return UserMapper.toUserDto(userRepository.updateUser(user));
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(
                    String.format("Пользователь с id=%s не найден", userId)
            );
        }
        userRepository.deleteUser(user.get());
    }
}
