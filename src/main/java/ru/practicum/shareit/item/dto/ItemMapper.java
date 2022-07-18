package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;

import java.util.Optional;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(Optional.of(item.isAvailable()))
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable().orElse(false));

        return item;
    }
}
