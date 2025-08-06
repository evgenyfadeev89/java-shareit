package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.PersonalRequestDto;
import ru.practicum.shareit.request.dto.PublicRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.NewRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@ImportAutoConfiguration({
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestDto requestDto;
    private NewRequest newRequest;
    private User requestor;
    private PublicRequestDto publicRequestDto;
    private PersonalRequestDto personalRequestDto;

    @BeforeEach
    void setUp() {
        requestor = new User(null, "Test User", "test@example.com");
        newRequest = new NewRequest("Test Request", requestor.getId(), null);
        requestDto = new RequestDto(1L,
                "Description",
                null,
                null);
        personalRequestDto = new PersonalRequestDto(1L,
                "Description",
                null,
                null);
        publicRequestDto = new PublicRequestDto(1L,
                "Description",
                null);
    }

    @Test
    void create() throws Exception {
        given(requestService.create(any(NewRequest.class), anyLong(), any())).willReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }

    @Test
    void findAllPersonal() throws Exception {
        given(requestService.findAllPersonal(anyLong())).willReturn(Collections.singletonList(personalRequestDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(personalRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(personalRequestDto.getDescription()));
    }

    @Test
    void findAll() throws Exception {
        given(requestService.findAll(anyLong())).willReturn(Collections.singletonList(publicRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(publicRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(publicRequestDto.getDescription()));
    }

    @Test
    void getRequestById() throws Exception {
        given(requestService.getRequestById(anyLong(), anyLong())).willReturn(personalRequestDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(personalRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(personalRequestDto.getDescription()));
    }
}