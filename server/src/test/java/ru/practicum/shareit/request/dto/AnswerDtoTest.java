package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        AnswerDto answerDto = new AnswerDto(1L, "Иван Иванов", 1L);
        String json = objectMapper.writeValueAsString(answerDto);
        assertThat(json).contains("\"id\":1,\"name\":\"Иван Иванов\",\"owner\":1");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"name\":\"Иван Иванов\",\"owner\":1}";
        AnswerDto answerDto = objectMapper.readValue(json, AnswerDto.class);
        assertThat(answerDto.getId()).isEqualTo(1L);
        assertThat(answerDto.getName()).isEqualTo("Иван Иванов");
    }
}
