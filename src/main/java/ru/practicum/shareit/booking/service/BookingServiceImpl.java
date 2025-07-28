package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.NewBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
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
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto create(NewBooking newBooking, Long userId) {
        if (userId == null) {
            throw new NotFoundException("ID пользователя не указан");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));

        Item item = itemRepository.findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException("Такой вещи для бронирования не существует"));

        if (item.getAvailable().equals(false)) {
            throw new ConditionsNotMetException("Вещь недоступна для бронирования");
        }
        if (newBooking.getStart().equals(newBooking.getEnd())) {
            throw new ValidationException("Время начала и конец бронирования равны");
        }
        if (newBooking.getStart() == null) {
            throw new ValidationException("Время начала бронирования не указано");
        }
        if (newBooking.getEnd() == null) {
            throw new ValidationException("Время конца бронирования не указано");
        }

        newBooking.setBooker(userId);
        Booking booking = bookingMapper.toBooking(newBooking);

        booking.setBooker(booker);
        booking.setItem(item);

        bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    public BookingDto approveBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ConditionsNotMetException("Запрос не от владельца вещи");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingMapper.toBookingDto(booking);
    }

    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId) &&
                !booking.getBooker().getId().equals(userId)) {
            throw new ConditionsNotMetException("Запрос не от владельца вещи/инициатора запроса бронирования");
        }

        return bookingMapper.toBookingDto(booking);
    }

    public List<BookingDto> getUserBookings(Long userId, BookingState state) {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);

        return bookings.stream()
                .filter(booking -> filterByState(booking, state))
                .map(bookingMapper::toBookingDto)
                .toList();
    }


    private boolean filterByState(Booking booking, BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        return switch (state) {
            case ALL -> true;
            case CURRENT -> booking.getStart().isBefore(now) && (booking.getEnd().isAfter(now) ||
                    booking.getEnd() == null);
            case PAST -> booking.getEnd().isBefore(now);
            case FUTURE -> booking.getStart().isAfter(now);
            case WAITING -> booking.getStatus() == Status.WAITING;
            case REJECTED -> booking.getStatus() == Status.REJECTED;
        };
    }

    public List<BookingDto> getOwnerBookings(Long userId, BookingState state) {
        if (!itemRepository.existsByOwnerId(userId)) {
            throw new NotFoundException("У пользователя нет вещей для бронирования");
        }

        List<Long> itemIds = itemRepository.findByOwnerId(userId)
                .stream()
                .map(Item::getId)
                .toList();

        return bookingRepository.findByItemIdIn(itemIds)
                .stream()
                .filter(booking -> filterByState(booking, state))
                .map(bookingMapper::toOwnerItemsBookingDto)
                .toList();
    }
}

