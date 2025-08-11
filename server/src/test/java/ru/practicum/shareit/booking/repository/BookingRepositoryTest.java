package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User booker;
    private User owner;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner", "owner@example.com");
        owner = userRepository.save(owner);

        booker = new User(null, "Booker", "booker@example.com");
        booker = userRepository.save(booker);

        item1 = new Item(null, "Item1", "Desc1", true, owner, null);
        item1 = itemRepository.save(item1);

        item2 = new Item(null, "Item2", "Desc2", true, owner, null);
        item2 = itemRepository.save(item2);

        booking1 = new Booking();
        booking1.setItem(item1);
        booking1.setBooker(booker);
        booking1.setStart(LocalDateTime.now().minusDays(5));
        booking1.setEnd(LocalDateTime.now().minusDays(3));
        booking1.setStatus(Status.APPROVED);
        bookingRepository.save(booking1);

        booking2 = new Booking();
        booking2.setItem(item2);
        booking2.setBooker(booker);
        booking2.setStart(LocalDateTime.now().plusDays(2));
        booking2.setEnd(LocalDateTime.now().plusDays(4));
        booking2.setStatus(Status.WAITING);
        bookingRepository.save(booking2);

        User otherUser = new User(null, "Other", "other@example.com");
        otherUser = userRepository.save(otherUser);

        booking3 = new Booking();
        booking3.setItem(item1);
        booking3.setBooker(otherUser);
        booking3.setStart(LocalDateTime.now().minusDays(1));
        booking3.setEnd(LocalDateTime.now().plusDays(1));
        booking3.setStatus(Status.APPROVED);
        bookingRepository.save(booking3);
    }

    @Test
    void findByBookerIdOrderByStartDescShouldReturnAllBookingsOfBookerOrdered() {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(booker.getId());
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertTrue(bookings.get(0).getStart().isAfter(bookings.get(1).getStart()) ||
                bookings.get(0).getStart().isEqual(bookings.get(1).getStart()));
    }

    @Test
    void findByItemIdInShouldReturnBookingsForGivenItemIdsOrdered() {
        List<Long> itemIds = List.of(item1.getId());
        List<Booking> bookings = bookingRepository.findByItemIdIn(itemIds);
        assertFalse(bookings.isEmpty());
        assertTrue(bookings.stream().allMatch(b -> b.getItem().getId().equals(item1.getId())));

        assertTrue(bookings.get(0).getStart().isAfter(bookings.get(1).getStart()) ||
                bookings.get(0).getStart().isEqual(bookings.get(1).getStart()));
    }

    @Test
    void existsByBookerIdAndItemIdAndEndBeforeShouldReturnTrueIfSuchBookingExists() {
        boolean exists = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(
                booker.getId(),
                item1.getId(),
                LocalDateTime.now()
        );
        assertTrue(exists);

        boolean notExists = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(
                booker.getId(),
                item2.getId(),
                LocalDateTime.now()
        );
        assertFalse(notExists);
    }

    @Test
    void findByItemIdShouldReturnBookingsForItem() {
        List<Booking> bookings = bookingRepository.findByItemId(item1.getId());
        assertNotNull(bookings);
        assertTrue(bookings.size() >= 2);
        assertTrue(bookings.stream().allMatch(b -> b.getItem().getId().equals(item1.getId())));
    }
}
