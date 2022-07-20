package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exceptions.BookingBadRequestException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static BookingDtoOut toBookingDto(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }

    public static BookingForItemDtoOut toBookingForItemDto(Booking booking) {
        if (booking == null) return null;
        return BookingForItemDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static List<BookingDtoOut> toBookingDto(Iterable<Booking> bookings) {
        List<BookingDtoOut> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            dtos.add(toBookingDto(booking));
        }
        return dtos;
    }

    public static Booking toBooking(BookingDtoIn bookingDtoIn,
                                    ItemRepository itemRepository) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoIn.getStart());
        booking.setEnd(bookingDtoIn.getEnd());
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new BookingBadRequestException("В бронировании неверно заданы даты");
        }
        booking.setItem(itemRepository.findById(bookingDtoIn.getItemId()).orElseThrow(
                () -> new ItemNotFoundException(String.format("Item с id=%s не найден", bookingDtoIn.getItemId()))
        ));

        return booking;
    }
}
