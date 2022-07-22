package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusOfBooking;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingBadRequestException;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOut saveBooking(Long bookerId, BookingDtoIn bookingDtoIn) {
        log.debug("Запрос saveBooking для bookerId - {} и itemId - {}", bookerId, bookingDtoIn.getItemId());

        Booking booking = BookingMapper.toBooking(bookingDtoIn, itemRepository);

        if (!booking.getItem().isAvailable()) {
            throw new BookingBadRequestException(
                    String.format("Бронирование недоступной вещи %s", booking.getItem().getId()));
        }

        if (booking.getItem().getOwner().getId().equals(bookerId)) {
            throw new BookingNotFoundException(
                    String.format("Бронирование вещи %s ее владельцем %s", booking.getItem().getId(), bookerId));
        }

        booking.setStatus(StatusOfBooking.WAITING);
        booking.setBooker(userRepository.findById(bookerId).orElseThrow(
                () -> new UserNotFoundException(String.format("Букер с id=%s не найден", bookerId))
        ));

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOut updateStatusOfBooking(Long bookingId, Long userId, boolean approved) {
        log.debug("Запрос updateStatusOfBooking для booking - {} от user - {}. Подтвержден - {}",
                bookingId, userId, approved);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException(String.format("Booking с id=%s не найден", bookingId)));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BookingNotFoundException(String.format("Booking с id=%s не принадлежит user %s",
                    bookingId, userId));
        }

        if (!booking.getStatus().equals(StatusOfBooking.WAITING)) {
            throw  new BookingBadRequestException(String.format("Booking с id=%s имеет неподходящий статус",
                    bookingId));
        }

        booking.setStatus(approved ? StatusOfBooking.APPROVED : StatusOfBooking.REJECTED);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOut getBookingById(Long userId, Long bookingId) {
        log.debug("Запрос getBookingById для userId - {} и bookingId - {}", userId, bookingId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundException(String.format("Booking с id=%s не найден", bookingId)));

        if ((!booking.getItem().getOwner().getId().equals(userId)) && (!booking.getBooker().getId().equals(userId))) {
            throw new BookingNotFoundException(String.format("Нет прав на просмотр booking=%s для user %s",
                    bookingId, userId));
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDtoOut> getBookingsForUser(Long userId, StateOfBooking state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("Booker с id=%s не найден", userId)));
        List<Booking> bookings;
        LocalDateTime date = LocalDateTime.now();
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.currentBookingByBooker(user, date);
                break;
            case PAST:
                bookings = bookingRepository.pastBookingByBooker(user, date);
                break;
            case FUTURE:
                bookings = bookingRepository.futureBookingByBooker(user, date);
                break;
            case WAITING:
                bookings = bookingRepository.bookingsByBookerAndStatus(user,
                        StatusOfBooking.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.bookingsByBookerAndStatus(user,
                        StatusOfBooking.REJECTED);
                break;
            default:
                bookings = bookingRepository.findBookingsByBookerOrderByStartDesc(user);
        }
        return BookingMapper.toBookingDto(bookings);
    }

    @Override
    public List<BookingDtoOut> getBookingsForItemsOwner(Long userId, StateOfBooking state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("OwnerItem с id=%s не найден", userId)));
        List<Item> items = itemRepository.findByOwner(user);
        if (items.size() == 0) {
            throw new BookingNotFoundException(String.format("Не найдены bookings для user %s", userId));
        }

        List<Booking> bookings;
        LocalDateTime date = LocalDateTime.now();
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.currentBookingByItems(items, date);
                break;
            case PAST:
                bookings = bookingRepository.pastBookingByItems(items, date);
                break;
            case FUTURE:
                bookings = bookingRepository.futureBookingByItems(items, date);
                break;
            case WAITING:
                bookings = bookingRepository.bookingsByItemsAndStatus(items,
                        StatusOfBooking.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.bookingsByItemsAndStatus(items,
                        StatusOfBooking.REJECTED);
                break;
            default:
                bookings = bookingRepository.findBookingsByItemInOrderByStartDesc(items);
        }
        return BookingMapper.toBookingDto(bookings);
    }
}
