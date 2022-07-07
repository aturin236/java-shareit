package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
    private static long idCounter;
    private final List<User> users = new ArrayList<>();

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return users.stream()
                .filter(x -> x.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User saveUser(User user) {
        user.setId(getId());
        users.add(user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        return user;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }

    private long getId() {
        return ++idCounter;
    }
}
