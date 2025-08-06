package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AllItemDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        AllItemDto allItemDto = new AllItemDto(1L,
                "Test allItemDto",
                "Description",
                true,
                null,
                null,
                null,
                null,
                null);
        String json = objectMapper.writeValueAsString(allItemDto);
        assertThat(json).contains("\"name\":\"Test allItemDto\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1," +
                "\"name\":\"Test allItemDto\"," +
                "\"description\":\"Description\"," +
                "\"available\":true," +
                "\"owner\":null," +
                "\"request\":null," +
                "\"nextBooking\":null," +
                "\"lastBooking\":null," +
                "\"comments\":null}";
        AllItemDto allItemDto = objectMapper.readValue(json, AllItemDto.class);
        assertThat(allItemDto.getName()).isEqualTo("Test allItemDto");
    }
}
