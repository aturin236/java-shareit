package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.StatusOfBooking;

import java.time.LocalDate;

@Data
@Builder
public class BookingDto {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Long itemId;
    private Long bookerId;
    private StatusOfBooking status;
}
