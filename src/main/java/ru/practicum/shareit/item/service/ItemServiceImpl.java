package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.AllItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    public List<AllItemDto> findAll(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        List<Long> itemIds = items.stream().map(Item::getId).toList();

        List<Booking> allBookings = bookingRepository.findByItemIdIn(itemIds);

        List<Comment> allComments = commentRepository.findByItemIdIn(itemIds);

        Map<Long, List<Booking>> bookingsByItem = allBookings.stream()
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        Map<Long, List<Comment>> commentsByItem = allComments.stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId()));

        return items.stream()
                .map(item -> {
                    AllItemDto dto = itemMapper.toAllItemDto(item);
                    List<Booking> itemBookings = bookingsByItem.getOrDefault(item.getId(), List.of());
                    List<Comment> itemComments = commentsByItem.getOrDefault(item.getId(), List.of());

                    Optional<Booking> lastBooking = itemBookings.stream()
                            .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                            .max(Comparator.comparing(Booking::getEnd));

                    Optional<Booking> nextBooking = itemBookings.stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                            .min(Comparator.comparing(Booking::getStart));

                    lastBooking.ifPresent(booking -> {
                        dto.setLastBooking(booking.getEnd());
                    });

                    nextBooking.ifPresent(booking -> {
                        dto.setNextBooking(booking.getStart());
                    });

                    dto.setComments(itemComments.stream()
                            .map(commentMapper::toCommentDto)
                            .collect(Collectors.toList()));

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public AllItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        AllItemDto allItemDto = itemMapper.toAllItemDto(item);

        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

        allItemDto.setComments(comments);

        List<Booking> allBookings = bookingRepository.findByItemId(itemId);

        Optional<Booking> lastBooking = allBookings.stream()
                .max(Comparator.comparing(Booking::getEnd));

        Optional<Booking> nextBooking = allBookings.stream()
                .min(Comparator.comparing(Booking::getStart));

        lastBooking.ifPresent(booking -> {
            if (booking.getEnd().isAfter(LocalDateTime.now())) {
                allItemDto.setLastBooking(booking.getEnd());
            } else {
                allItemDto.setLastBooking(null);
            }
        });

        nextBooking.ifPresent(booking -> {
            if (booking.getEnd().isAfter(LocalDateTime.now())) {
                allItemDto.setNextBooking(booking.getStart());
            } else {
                allItemDto.setNextBooking(null);
            }
        });

        return allItemDto;
    }


    @Override
    public List<ItemDto> getItemByName(String text) {
        return itemRepository.findByName(text)
                .stream()
                .filter(itm -> itm.getAvailable().equals(true))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ItemDto create(NewItem newItem, Long userId) {
        if (userId == null) {
            throw new ConditionsNotMetException("ID пользователя не указан");
        }
        if (!newItem.hasValidName()) {
            throw new ConditionsNotMetException("Имя должно быть указано");
        }
        if (!newItem.hasValidDescription()) {
            throw new ConditionsNotMetException("Описание должно быть указано");
        }
        if (!newItem.hasValidAvailable()) {
            throw new ConditionsNotMetException("Статус должен быть указан");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует");
        }

        newItem.setOwner(userId);
        Item item = itemMapper.toItem(newItem);
        item = itemRepository.save(item);

        return itemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(Long itemId, UpdateItem updateItem, Long userId) {
        if (userId == null) {
            throw new ConditionsNotMetException("ID пользователя не указан");
        }
        if (!itemRepository.findById(itemId).get().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Редактировать может только владелец вещи");
        }

        Item updatedItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        itemMapper.updateItemFields(updateItem, updatedItem);

        updatedItem = itemRepository.save(updatedItem);

        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public CommentDto addComment(Long itemId, NewCommentRequest commentRequest, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        boolean hasBookings = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(
                userId,
                itemId,
                LocalDateTime.now());

        if (!hasBookings) {
            throw new ConditionsNotMetException("Вы не можете оставить отзыв на эту вещь");
        }

        Comment comment = commentMapper.toComment(commentRequest, item, author);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }
}
