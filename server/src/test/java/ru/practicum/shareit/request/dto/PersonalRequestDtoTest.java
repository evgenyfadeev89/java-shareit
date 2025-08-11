package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonalRequestDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSerialize() throws Exception {
        PersonalRequestDto personalRequestDto = new PersonalRequestDto(1L,
                "Description",
                null,
                null);
        String json = objectMapper.writeValueAsString(personalRequestDto);
        assertThat(json).contains("\"id\":1,\"description\":\"Description\",\"created\":null,\"items\":null");
    }

    @Test
    void testDeserialize() throws Exception {
        String json = "{\"id\":1,\"description\":\"Description\",\"created\":null,\"items\":null}";
        PersonalRequestDto personalRequestDto = objectMapper.readValue(json, PersonalRequestDto.class);
        assertThat(personalRequestDto.getId()).isEqualTo(1L);
        assertThat(personalRequestDto.getDescription()).isEqualTo("Description");
    }
}
