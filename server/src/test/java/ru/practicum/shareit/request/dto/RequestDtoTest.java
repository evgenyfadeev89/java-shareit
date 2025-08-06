package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class RequestDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        RequestDto requestDto = new RequestDto(1L,
                "Description",
                null,
                null);
        String json = objectMapper.writeValueAsString(requestDto);
        assertThat(json).contains("\"id\":1," +
                "\"description\":\"Description\"," +
                "\"requestor\":null," +
                "\"created\":null");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1," +
                "\"description\":\"Description\"," +
                "\"requestor\":null," +
                "\"created\":null}";
        RequestDto requestDto = objectMapper.readValue(json, RequestDto.class);
        assertThat(requestDto.getId()).isEqualTo(1L);
        assertThat(requestDto.getDescription()).isEqualTo("Description");
    }
}