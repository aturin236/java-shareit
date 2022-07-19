package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    List<BookingDtoOut> getBookingsForUser(Long userId, StateOfBooking state);

    List<BookingDtoOut> getBookingsForItemsOwner(Long userId, StateOfBooking state);

    BookingDtoOut getBookingById(Long userId, Long bookingId);

    BookingDtoOut saveBooking(Long bookerId, BookingDtoIn bookingDtoIn);

    BookingDtoOut updateStatusOfBooking(Long bookingId, Long userId, boolean approved);
}
