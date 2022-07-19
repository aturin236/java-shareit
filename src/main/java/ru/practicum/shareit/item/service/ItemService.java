package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllItems();

    List<ItemWithBookingDto> getAllItemsByUser(Long userId);

    ItemDto getItemById(Long userId, Long itemId);

    ItemDto saveItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> searchItem(String text);

    CommentDtoOut saveComment(Long userId, Long itemId, CommentDtoIn commentDtoIn);
}
