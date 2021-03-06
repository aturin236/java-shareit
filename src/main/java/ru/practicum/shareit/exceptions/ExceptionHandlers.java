package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler({UserAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handle409(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({ItemForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handle403(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({BookingBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle400(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class, BookingNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle404(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}
