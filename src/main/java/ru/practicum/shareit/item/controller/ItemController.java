package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.AllItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.NewCommentRequest;
import ru.practicum.shareit.item.model.NewItem;
import ru.practicum.shareit.item.model.UpdateItem;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl itemService;

    @GetMapping
    public ResponseEntity<List<AllItemDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<AllItemDto> items = itemService.findAll(userId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(items);
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<AllItemDto> findItemById(@PathVariable("itemId") Long itemId,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemService.getItemById(itemId, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> findItemByName(@RequestParam("text") String text) {
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(itemService.getItemByName(text));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDto> create(@RequestBody NewItem newItem,
                                          @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        return ResponseEntity.ok(itemService.create(newItem, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@PathVariable("itemId") Long itemId,
                                          @Valid @RequestBody UpdateItem updateItem,
                                          @RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId) {
        return ResponseEntity.ok(itemService.update(itemId, updateItem, userId));
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDto> createComment(@PathVariable("itemId") Long itemId,
                                                    @RequestBody NewCommentRequest commentRequest,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemService.addComment(itemId, commentRequest, userId));
    }
}
