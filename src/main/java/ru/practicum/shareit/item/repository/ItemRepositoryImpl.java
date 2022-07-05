package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private static long idCounter;
    private final List<Item> items = new ArrayList<>();

    @Override
    public List<Item> getAllItem() {
        return items;
    }

    @Override
    public List<Item> getAllItemByUser(Long userId) {
        return items.stream()
                .filter(x-> Objects.equals(x.getOwner().getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return items.stream()
                .filter(x-> Objects.equals(x.getId(), itemId))
                .findFirst();
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(getId());
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        return item;
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isEmpty()) return new ArrayList<>();

        String regex = ".*" + text + ".*";

        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        return items.stream()
                .filter(Item::isAvailable)
                .filter(x-> pattern.matcher(x.getName()).matches() || pattern.matcher(x.getDescription()).matches())
                .collect(Collectors.toList());
    }

    private long getId() {
        return ++idCounter;
    }
}
