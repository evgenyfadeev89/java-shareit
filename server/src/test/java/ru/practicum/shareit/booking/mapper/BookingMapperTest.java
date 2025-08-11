package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    void setUp() {
        bookingMapper = BookingMapper.INSTANCE;
    }

    @Test
    void toBookingDtoShouldMapBookingToBookingDto() {
        User owner = new User(1L, "Owner", "owner@example.com");
        User booker = new User(2L, "Booker", "booker@example.com");
        Item item = new Item(5L, "Item Name",
                "Item Description",
                true,
                owner,
                null);
        Booking booking = new Booking(
                10L,
                LocalDateTime.of(2023, 8, 6, 14, 0),
                LocalDateTime.of(2023, 8, 7, 14, 0),
                item,
                booker,
                Status.APPROVED
        );

        BookingDto dto = bookingMapper.toBookingDto(booking);

        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getStart(), dto.getStart());
        assertEquals(booking.getEnd(), dto.getEnd());
        assertNotNull(dto.getItem());
        assertEquals(item.getId(), dto.getItem().getId());
        assertEquals(item.getName(), dto.getItem().getName());
        assertNotNull(dto.getBooker());
        assertEquals(booker.getId(), dto.getBooker().getId());
        assertEquals(booker.getName(), dto.getBooker().getName());
        assertEquals(booking.getStatus(), dto.getStatus());

        assertEquals(owner.getId(), dto.getItem().getOwner());
    }

    @Test
    void toBookingShouldMapNewBookingToBooking() {
        NewBooking newBooking = new NewBooking();
        newBooking.setItemId(7L);
        newBooking.setBooker(3L);
        newBooking.setStart(LocalDateTime.of(2023, 8, 8, 10, 0));
        newBooking.setEnd(LocalDateTime.of(2023, 8, 9, 10, 0));
        newBooking.setStatus(Status.WAITING);

        Booking booking = bookingMapper.toBooking(newBooking);

        assertNotNull(booking);
        assertNull(booking.getId());
        assertEquals(newBooking.getStart(), booking.getStart());
        assertEquals(newBooking.getEnd(), booking.getEnd());
        assertEquals(newBooking.getStatus(), booking.getStatus());

        assertNotNull(booking.getItem());
        assertEquals(newBooking.getItemId(), booking.getItem().getId());

        assertNotNull(booking.getBooker());
        assertEquals(newBooking.getBooker(), booking.getBooker().getId());
    }

    @Test
    void toOwnerItemsBookingDtoShouldMapBookingToBookingDto() {
        User owner = new User(4L, "OwnerName", "owner2@example.com");
        User booker = new User(5L, "BookerName", "booker2@example.com");
        Item item = new Item(8L, "Item2", "Item Desc2", true, owner, null);
        Booking booking = new Booking(
                20L,
                LocalDateTime.of(2023, 8, 10, 9, 0),
                LocalDateTime.of(2023, 8, 11, 9, 0),
                item,
                booker,
                Status.REJECTED
        );

        BookingDto dto = bookingMapper.toOwnerItemsBookingDto(booking);

        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getStart(), dto.getStart());
        assertEquals(booking.getEnd(), dto.getEnd());
        assertNotNull(dto.getItem());
        assertEquals(item.getId(), dto.getItem().getId());
        assertEquals(item.getName(), dto.getItem().getName());
        assertNotNull(dto.getBooker());
        assertEquals(booker.getId(), dto.getBooker().getId());
        assertEquals(booking.getStatus(), dto.getStatus());

        assertEquals(owner.getId(), dto.getItem().getOwner());
    }
}
