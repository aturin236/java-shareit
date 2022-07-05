package ru.practicum.shareit.exceptions;

public class ItemForbiddenException extends RuntimeException {
    public ItemForbiddenException(String message) {
        super(message);
    }
}
