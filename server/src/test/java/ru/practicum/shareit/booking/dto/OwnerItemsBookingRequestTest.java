package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OwnerItemsBookingRequestTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        OwnerItemsBookingRequest ownerItemsBookingRequest = new OwnerItemsBookingRequest(
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                null,
                Status.WAITING
        );
        String json = objectMapper.writeValueAsString(ownerItemsBookingRequest);
        assertThat(json).contains("\"item\":null");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"item\":null,\"start\":\"2023-10-10T10:00:00\",\"end\":\"2023-10-11T10:00:00\"," +
                "\"booker\":null,\"status\":\"WAITING\"}";
        OwnerItemsBookingRequest ownerItemsBookingRequest = objectMapper.readValue(json, OwnerItemsBookingRequest.class);
        assertThat(ownerItemsBookingRequest.getStatus()).isEqualTo(Status.WAITING);
    }

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