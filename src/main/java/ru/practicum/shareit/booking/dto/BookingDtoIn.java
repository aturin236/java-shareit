package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoIn {
    @NotEmpty
    private LocalDateTime start;
    @NotEmpty
    private LocalDateTime end;
    @NotEmpty
    private Long itemId;
}
