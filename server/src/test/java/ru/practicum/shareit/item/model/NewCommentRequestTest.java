package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;


public class NewCommentRequestTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        NewCommentRequest newCommentRequest = new NewCommentRequest("Test comment");
        String json = objectMapper.writeValueAsString(newCommentRequest);
        assertThat(json).contains("\"text\":\"Test comment\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"text\":\"Test comment\"}";
        NewCommentRequest newCommentRequest = objectMapper.readValue(json, NewCommentRequest.class);
        assertThat(newCommentRequest.getText()).isEqualTo("Test comment");
    }
}
