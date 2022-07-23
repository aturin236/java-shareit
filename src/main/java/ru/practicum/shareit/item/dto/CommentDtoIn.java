package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentDtoIn {
    @NotEmpty
    private String text;
}
