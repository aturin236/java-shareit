package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Запрос getAllUsers");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.debug("Запрос getUserById по userId - {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с id=%s не найден", userId)));

        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.debug("Запрос saveUser по email - {}", userDto.getEmail());
        return UserMapper.toUserDto(
               userRepository.save(
                       UserMapper.toUser(userDto)
               )
       );
    }

    @Override
    public @Valid UserDto updateUser(Long userId, UserDto userDto) {
        log.debug("Запрос updateUser по userId - {}", userId);
        userRepository.findByEmail(userDto.getEmail()).ifPresent(
                x -> {
                    if (!x.getId().equals(userDto.getId())) {
                        throw new UserAlreadyExistException(
                                String.format("Пользователь с email=%s уже существует", userDto.getEmail())
                        );
                    }
                }
        );
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с id=%s не найден", userId)));

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        log.debug("Запрос deleteUser по userId - {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с id=%s не найден", userId)));
        userRepository.delete(user);
    }
}
