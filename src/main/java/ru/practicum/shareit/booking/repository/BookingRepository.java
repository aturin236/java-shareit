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
    List<Booking> currentBookingByBooker(User user, LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.booker = ?1 and b.end < ?2 " +
            "order by b.start desc")
    List<Booking> pastBookingByBooker(User user, LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.booker = ?1 and b.start > ?2 " +
            "order by b.start desc")
    List<Booking> futureBookingByBooker(User user, LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.booker = ?1 and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> bookingsByBookerAndStatus(User user, StatusOfBooking status);

    List<Booking> findBookingsByBookerOrderByStartDesc(User user);

    @Query("select b from Booking b " +
            "where b.item in (?1) and b.start <= ?2 and b.end >= ?2 " +
            "order by b.start desc")
    List<Booking> currentBookingByItems(List<Item> items, LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.item in (?1) and b.end < ?2 " +
            "order by b.start desc")
    List<Booking> pastBookingByItems(List<Item> items, LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.item in (?1) and b.start > ?2 " +
            "order by b.start desc")
    List<Booking> futureBookingByItems(List<Item> items, LocalDateTime date);

    @Query("select b from Booking b " +
            "where b.item in (?1) and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> bookingsByItemsAndStatus(List<Item> items, StatusOfBooking status);

    List<Booking> findBookingsByItemInOrderByStartDesc(List<Item> items);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 and b.end_date_time < ?2 " +
            "order by b.start_date_time desc LIMIT 1", nativeQuery = true)
    Optional<Booking> lastBooking(Long itemId, LocalDateTime date);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 and b.start_date_time > ?2 " +
            "order by b.start_date_time desc LIMIT 1", nativeQuery = true)
    Optional<Booking> nextBooking(Long itemId, LocalDateTime date);

    @Query(value = "select * from bookings b " +
            "where b.booker_id = ?1 and b.item_id = ?2 and b.end_date_time < ?3 " +
            "order by b.start_date_time desc LIMIT 1", nativeQuery = true)
    Optional<Booking> pastBookingByBookerAndItem(Long userId, Long itemId, LocalDateTime date);
}
