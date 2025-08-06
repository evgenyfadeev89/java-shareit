package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NewBookingTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        NewBooking newBooking = new NewBooking(
                1L,
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                Status.WAITING
        );
        String json = objectMapper.writeValueAsString(newBooking);
        assertThat(json).contains("\"itemId\":1");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2023-10-10T10:00:00\",\"end\":\"2023-10-11T10:00:00\"}";
        NewBooking newBooking = objectMapper.readValue(json, NewBooking.class);
        assertThat(newBooking.getItemId()).isEqualTo(1L);
    }
}