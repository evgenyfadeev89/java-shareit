package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OwnerItemsBookingRequestTest {

    @Test
    void testOwnerItemsBookingRequest() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);

        UserDto booker = new UserDto(1L, "Иван Иванов", "ivan.ivanov@example.com");
        Status status = Status.APPROVED;

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setItem(item);
        bookingDto.setBooker(booker);
        bookingDto.setStatus(status);

        assertEquals(1L, bookingDto.getId());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(item, bookingDto.getItem());
        assertEquals(booker, bookingDto.getBooker());
        assertEquals(status, bookingDto.getStatus());
    }
}