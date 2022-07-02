package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public void saveUser(User user) {
        if (userRepository.getUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistException(
                    String.format("Пользователь с email=%s уже существует", user.getEmail())
            );
        }
        userRepository.saveUser(user);
    }

    @Override
    public void updateUser(User user) {
        if (userRepository.getUserById(user.getId()).isPresent()) {
            throw new UserNotFoundException(
                    String.format("Пользователь с id=%s не найден", user.getId())
            );
        }
        userRepository.getUserByEmail(user.getEmail()).ifPresent(
                x -> {
                    if (!x.getId().equals(user.getId())) {
                        throw new UserAlreadyExistException(
                                String.format("Пользователь с email=%s уже существует", user.getEmail())
                        );
                    }
                }
        );
        userRepository.saveUser(user);
    }

    @Override
    public void deleteUser(User user) {
        if (userRepository.getUserById(user.getId()).isPresent()) {
            throw new UserNotFoundException(
                    String.format("Пользователь с id=%s не найден", user.getId())
            );
        }
        userRepository.deleteUser(user);
    }
}
