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

class BookingDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        BookingDto bookingDto = new BookingDto(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                new ItemDto(),
                new UserDto(),
                Status.WAITING);
        String json = objectMapper.writeValueAsString(bookingDto);
        assertThat(json).contains("\"id\":1", "\"status\":\"WAITING\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"start\":\"2025-01-01T10:00:00\",\"end\":\"2025-01-10T10:00:00\",\"status\":\"WAITING\"}";
        BookingDto bookingDto = objectMapper.readValue(json, BookingDto.class);
        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStatus()).isEqualTo(Status.WAITING);
    }
}