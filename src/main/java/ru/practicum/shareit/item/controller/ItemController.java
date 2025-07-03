package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.NewItemRequest;
import ru.practicum.shareit.item.model.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemServiceImpl;


import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;

    @GetMapping("/items")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.findAll(userId);
    }

    @GetMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto findItemById(@PathVariable("itemId") Long itemId) {
        return itemServiceImpl.getItemById(itemId);
    }

    @GetMapping("/items/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findItemByName(@RequestParam("text") String text) {
        return itemServiceImpl.getItemByName(text);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestBody NewItemRequest itemRequest,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.create(itemRequest, userId);
    }

    @PatchMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@PathVariable("itemId") Long itemId,
                          @Valid @RequestBody UpdateItemRequest updateItemRequest,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.update(itemId, updateItemRequest, userId);
    }
}
