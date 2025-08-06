package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        ItemDto itemDto = new ItemDto(1L,
                "Test Item",
                "Description",
                true,
                null,
                null,
                null);
        String json = objectMapper.writeValueAsString(itemDto);
        assertThat(json).contains("\"name\":\"Test Item\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Description\",\"available\":true}";
        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);
        assertThat(itemDto.getName()).isEqualTo("Test Item");
    }
}