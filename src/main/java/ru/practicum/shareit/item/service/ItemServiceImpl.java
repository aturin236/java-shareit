package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemForbiddenException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.getAllItem().stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        checkUserExist(userId);
        return itemRepository.getAllItemByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        checkUserExist(userId);
        Optional<Item> item = itemRepository.getItemById(itemId);
        if (item.isEmpty()) {
            throw new ItemNotFoundException(
                    String.format("Вещь с id=%s не найдена", itemId)
            );
        }

        return item.map(ItemMapper::toItemDto).orElse(null);
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = checkUserExist(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);

        return ItemMapper.toItemDto(itemRepository.saveItem(item));
    }

    @Override
    public @Valid ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        checkUserExist(userId);
        Optional<Item> itemOptional = itemRepository.getItemById(itemId);
        if (itemOptional.isEmpty()) {
            throw new ItemNotFoundException(
                    String.format("Вещь с id=%s не найдена", itemId)
            );
        }

        Item item = itemOptional.get();

        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new ItemForbiddenException(
                    String.format("Вещь с id=%s запрещено редактировать пользователью с id=%s", itemId, userId)
            );
        }

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());

        itemDto.getAvailable().ifPresent(item::setAvailable);

        return ItemMapper.toItemDto(itemRepository.updateItem(item));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User checkUserExist(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(
                    String.format("Пользователь с id=%s не найден", userId)
            );
        }

        return user.get();
    }
}