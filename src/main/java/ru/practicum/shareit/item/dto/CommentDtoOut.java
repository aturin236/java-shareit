package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoOut {
    private Long Id;
    private String text;
    private Item item;
    private User author;
    private LocalDateTime created;
}
