package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.NewItemRequest;
import ru.practicum.shareit.item.model.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> items = itemService.findAll(userId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.ok(items); // 200 OK
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> findItemById(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId)); // 200 OK
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> findItemByName(@RequestParam("text") String text) {
        return ResponseEntity.ok(itemService.getItemByName(text));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItemDto> create(@RequestBody NewItemRequest itemRequest,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemService.create(itemRequest, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@PathVariable("itemId") Long itemId,
                                          @Valid @RequestBody UpdateItemRequest updateItemRequest,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemService.update(itemId, updateItemRequest, userId));
    }
}
