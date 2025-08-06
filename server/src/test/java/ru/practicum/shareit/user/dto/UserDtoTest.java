package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto(1L, "Test User", "test@example.com");
        String json = objectMapper.writeValueAsString(userDto);
        assertThat(json).contains("\"id\":1", "\"name\":\"Test User\"", "\"email\":\"test@example.com\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";
        UserDto userDto = objectMapper.readValue(json, UserDto.class);
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("Test User");
        assertThat(userDto.getEmail()).isEqualTo("test@example.com");
    }
}