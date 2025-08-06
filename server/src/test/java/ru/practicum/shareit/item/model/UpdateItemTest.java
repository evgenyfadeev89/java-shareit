package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;


public class UpdateItemTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        UpdateItem updateItem = new UpdateItem("Test name", "Description", true);
        String json = objectMapper.writeValueAsString(updateItem);
        assertThat(json).contains("\"name\":\"Test name\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"name\":\"Test name\",\"description\":\"Description\"}";
        UpdateItem updateItem = objectMapper.readValue(json, UpdateItem.class);
        assertThat(updateItem.getName()).isEqualTo("Test name");
    }
}
