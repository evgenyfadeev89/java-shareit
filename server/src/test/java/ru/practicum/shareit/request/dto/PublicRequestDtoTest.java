package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;


public class PublicRequestDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        PublicRequestDto publicRequestDto = new PublicRequestDto(1L,
                "Description",
                null);
        String json = objectMapper.writeValueAsString(publicRequestDto);
        assertThat(json).contains("\"id\":1,\"description\":\"Description\",\"created\":null");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"description\":\"Description\",\"created\":null}";
        PublicRequestDto publicRequestDto = objectMapper.readValue(json, PublicRequestDto.class);
        assertThat(publicRequestDto.getId()).isEqualTo(1L);
        assertThat(publicRequestDto.getDescription()).isEqualTo("Description");
    }
}
