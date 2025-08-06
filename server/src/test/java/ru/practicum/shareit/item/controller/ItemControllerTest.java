package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.AllItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.NewCommentRequest;
import ru.practicum.shareit.item.model.NewItem;
import ru.practicum.shareit.item.model.UpdateItem;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@ImportAutoConfiguration({
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private NewItem newItem;
    private ItemDto itemDto;
    private NewCommentRequest newCommentRequest;
    private CommentDto commentDto;
    private UpdateItem updateItem;
    private AllItemDto allItemDto;

    @BeforeEach
    void setUp() {
        newItem = new NewItem("Test Item",
                "Description",
                true,
                null,
                null);
        newCommentRequest = new NewCommentRequest("Great item!");
        commentDto = new CommentDto(1L, "Great item!", "User", null);
        updateItem = new UpdateItem("Test Item",
                "Description",
                true);
        itemDto = new ItemDto(1L,
                "Test Item",
                "Description",
                true,
                null,
                null,
                null);
    }

    @Test
    void create() throws Exception {
        given(itemService.create(any(NewItem.class), anyLong())).willReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void update() throws Exception {
        given(itemService.update(anyLong(), any(UpdateItem.class), anyLong())).willReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void findItemById() throws Exception {
        given(itemService.getItemById(anyLong(), anyLong())).willReturn(allItemDto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        given(itemService.findAll(anyLong())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void findItemByName() throws Exception {
        given(itemService.getItemByName(any())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "Test"))
                .andExpect(status().isOk());
    }

    @Test
    void createComment() throws Exception {
        given(itemService.addComment(anyLong(), any(NewCommentRequest.class), anyLong())).willReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentDto.getText()));
    }
}