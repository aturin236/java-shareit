package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
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
    public void saveUser(User user) {
        users.add(user);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
}
