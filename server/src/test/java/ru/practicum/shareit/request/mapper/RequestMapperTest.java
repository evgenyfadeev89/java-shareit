package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.NewRequest;
import ru.practicum.shareit.request.model.Request;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {

    private RequestMapper requestMapper;

    @BeforeEach
    void setUp() {
        requestMapper = RequestMapper.INSTANCE;
    }

    @Test
    void toPersonalRequestDtoShouldMapFieldsCorrectly() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Request Description");
        request.setCreated(LocalDateTime.of(2023, 8, 1, 12, 30));
        request.setRequestor(new ru.practicum.shareit.user.model.User(10L,
                "John",
                "john@example.com"));

        PersonalRequestDto dto = requestMapper.toPersonalRequestDto(request);

        assertNotNull(dto);
        assertEquals(request.getId(), dto.getId());
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getCreated(), dto.getCreated());
        assertNull(dto.getItems());

//        // Если PersonalRequestDto содержит список ответов (например, answers)
//        if (dto.getItems() != null) {
//            assertTrue(dto.getItems().isEmpty());
//        }
    }

    @Test
    void toRequestDtoShouldMapFieldsCorrectly() {
        Request request = new Request();
        request.setId(2L);
        request.setDescription("Another Description");
        request.setCreated(LocalDateTime.now());

        RequestDto dto = requestMapper.toRequestDto(request);

        assertNotNull(dto);
        assertEquals(request.getId(), dto.getId());
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getCreated(), dto.getCreated());
    }

    @Test
    void toRequest_ShouldMapFieldsCorrectly() {
        NewRequest newRequest = new NewRequest();
        newRequest.setDescription("New request description");
        newRequest.setRequestor(20L);

        Request result = requestMapper.toRequest(newRequest);

        assertNotNull(result);
        assertEquals(newRequest.getDescription(), result.getDescription());
        assertEquals(newRequest.getRequestor(), result.getRequestor().getId());
    }

    @Test
    void toAnswerDtoShouldMapFieldsCorrectly() {
        Item item = new Item();
        item.setId(33L);
        item.setName("Item name");
        item.setDescription("Item description");
        item.setAvailable(true);
        item.setOwner(new ru.practicum.shareit.user.model.User(77L,
                "Owner name",
                "owner@example.com"));
        item.setRequest(new Request());
        item.getRequest().setId(5L);

        AnswerDto dto = requestMapper.toAnswerDto(item);

        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getOwner().getId(), dto.getOwner());
    }

    @Test
    void toPublicRequestDto_ShouldMapFieldsCorrectly() {
        Request request = new Request();
        request.setId(5L);
        request.setDescription("Public request description");
        request.setCreated(LocalDateTime.now());

        PublicRequestDto dto = requestMapper.toPublicRequestDto(request);

        assertNotNull(dto);
        assertEquals(request.getId(), dto.getId());
        assertEquals(request.getDescription(), dto.getDescription());
        assertEquals(request.getCreated(), dto.getCreated());
    }
}
