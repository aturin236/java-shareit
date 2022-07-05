package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAllItem();
    List<Item> getAllItemByUser(Long userId);
    Optional<Item> getItemById(Long itemId);
    Item saveItem(Item item);
    Item updateItem(Item item);
    List<Item> searchItems(String text);
}
