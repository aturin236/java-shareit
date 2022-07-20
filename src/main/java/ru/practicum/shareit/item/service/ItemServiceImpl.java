package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingBadRequestException;
import ru.practicum.shareit.exceptions.ItemForbiddenException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemWithBookingDto> getAllItemsByUser(Long userId) {
        log.debug("Запрос getAllItemsByUser по userId - {}", userId);
        User user = checkUserExist(userId);
        LocalDateTime date = LocalDateTime.now();

        List<ItemWithBookingDto> list = new ArrayList<>();
        for (Item item : itemRepository.findByOwner(user)) {
            ItemWithBookingDto itemWithBookingDto = ItemMapper.toItemWithBookingDto(item);

            if (item.getOwner().getId().equals(userId)) {
                itemWithBookingDto.setLastBooking(
                        BookingMapper.toBookingForItemDto(
                                bookingRepository.findFirstByItemAndEndBeforeOrderByEndDesc(item, date).orElse(null)));
                itemWithBookingDto.setNextBooking(
                        BookingMapper.toBookingForItemDto(
                                bookingRepository.findFirstByItemAndStartAfterOrderByStartAsc(item, date).orElse(null)));
            }
            itemWithBookingDto.setComments(CommentMapper.toCommentDtoOut(commentRepository.findCommentsByItem(item)));

            list.add(itemWithBookingDto);
        }
        return list;
    }

    @Override
    public ItemWithBookingDto getItemById(Long userId, Long itemId) {
        log.debug("Запрос getItemById по userId - {} и itemId - {}", userId, itemId);
        checkUserExist(userId);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Item item = itemOptional.orElseThrow(
                () -> new ItemNotFoundException(String.format("Вещь с id=%s не найдена", itemId)));

        LocalDateTime date = LocalDateTime.now();

        List<CommentDtoOut> commentDtoOuts = CommentMapper.toCommentDtoOut(commentRepository.findCommentsByItem(item));

        ItemWithBookingDto itemWithBookingDto = ItemMapper.toItemWithBookingDto(item);

        if (item.getOwner().getId().equals(userId)) {
            itemWithBookingDto.setLastBooking(
                    BookingMapper.toBookingForItemDto(
                            bookingRepository.findFirstByItemAndEndBeforeOrderByEndDesc(item, date).orElse(null)));
            itemWithBookingDto.setNextBooking(
                    BookingMapper.toBookingForItemDto(
                            bookingRepository.findFirstByItemAndStartAfterOrderByStartAsc(item, date).orElse(null)));
        }
        itemWithBookingDto.setComments(commentDtoOuts);

        return itemWithBookingDto;
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        log.debug("Запрос saveItem по userId - {} для вещи - {}", userId, itemDto.getName());
        User user = checkUserExist(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public @Valid ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("Запрос updateItem по userId - {} и itemId - {}", userId, itemId);
        checkUserExist(userId);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        Item item = itemOptional.orElseThrow(
                () -> new ItemNotFoundException(String.format("Вещь с id=%s не найдена", itemId)));

        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new ItemForbiddenException(
                    String.format("Вещь с id=%s запрещено редактировать пользователью с id=%s", itemId, userId)
            );
        }

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());

        itemDto.getAvailable().ifPresent(item::setAvailable);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        log.debug("Запрос searchItem по строке - {}", text);
        if (text.isEmpty()) return new ArrayList<>();

        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User checkUserExist(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с id=%s не найден", userId)));
    }

    @Override
    public CommentDtoOut saveComment(Long userId, Long itemId, CommentDtoIn commentDtoIn) {
        log.debug("Запрос saveComment с текстом - {}, userId - {}, itemId - {}", commentDtoIn.getText(), userId, itemId);

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException(String.format("Item с id=%s не найден", itemId)));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User с id=%s не найден", userId)));

        bookingRepository.findFirstByBookerAndItemAndEndIsBefore(user, item, LocalDateTime.now()).orElseThrow(
                () -> new BookingBadRequestException(String.format(
                        "User с id=%s не не брал item %s в аренду", userId, itemId)));

        Comment comment = CommentMapper.toComment(commentDtoIn);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDtoOut(commentRepository.save(comment));
    }
}
