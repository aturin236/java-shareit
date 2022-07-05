package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllItems();
    List<ItemDto> getAllItemsByUser(Long userId);
    ItemDto getItemById(Long userId, Long itemId);
    ItemDto saveItem(Long userId, ItemDto itemDto);
    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
    List<ItemDto> searchItem(String text);
}
