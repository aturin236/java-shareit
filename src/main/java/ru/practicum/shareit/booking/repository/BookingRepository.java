package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusOfBooking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b " +
            "where b.booker = ?1 and b.start <= ?2 and b.end >= ?2 " +
            "order by b.start desc")
    List<Booking> findCurrentBookingByBooker(User user, LocalDateTime date);

    List<Booking> findBookingsByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime date);

    List<Booking> findBookingsByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime date);

    List<Booking> findBookingsByBookerAndStatusOrderByStartDesc(User user, StatusOfBooking status);

    List<Booking> findBookingsByBookerOrderByStartDesc(User user);

    @Query("select b from Booking b " +
            "where b.item in (?1) and b.start <= ?2 and b.end >= ?2 " +
            "order by b.start desc")
    List<Booking> findCurrentBookingByItems(List<Item> items, LocalDateTime date);

    List<Booking> findBookingsByItemInAndEndIsBeforeOrderByStartDesc(List<Item> items, LocalDateTime date);

    List<Booking> findBookingsByItemInAndStartIsAfterOrderByStartDesc(List<Item> items, LocalDateTime date);

    List<Booking> findBookingsByItemInAndStatusOrderByStartDesc(List<Item> items, StatusOfBooking status);

    List<Booking> findBookingsByItemInOrderByStartDesc(List<Item> items);

    Optional<Booking> findFirstByItemAndEndBeforeOrderByEndDesc(Item item, LocalDateTime date);

    Optional<Booking> findFirstByItemAndStartAfterOrderByStartAsc(Item item, LocalDateTime date);

    Optional<Booking> findFirstByBookerAndItemAndEndIsBefore(User user, Item item, LocalDateTime date);
}
