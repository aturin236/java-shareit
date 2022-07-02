package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
}
