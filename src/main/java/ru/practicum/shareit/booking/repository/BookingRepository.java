package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusOfBooking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            User user, LocalDateTime date1, LocalDateTime date2);

    List<Booking> findBookingsByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime date);

    List<Booking> findBookingsByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime date);

    List<Booking> findBookingsByBookerAndStatusOrderByStartDesc(User user, StatusOfBooking status);

    List<Booking> findBookingsByBookerOrderByStartDesc(User user);

    List<Booking> findBookingsByItemInAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            List<Item> items, LocalDateTime date1, LocalDateTime date2);

    List<Booking> findBookingsByItemInAndEndIsBeforeOrderByStartDesc(List<Item> items, LocalDateTime date);

    List<Booking> findBookingsByItemInAndStartIsAfterOrderByStartDesc(List<Item> items, LocalDateTime date);

    List<Booking> findBookingsByItemInAndStatusOrderByStartDesc(List<Item> items, StatusOfBooking status);

    List<Booking> findBookingsByItemInOrderByStartDesc(List<Item> items);
}
