package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.NewItemRequest;
import ru.practicum.shareit.item.model.UpdateItemRequest;

import java.util.List;


public interface ItemService {

    List<ItemDto> findAll(Long userId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemByName(String text);

    ItemDto create(NewItemRequest request, Long userId);

    ItemDto update(Long itemId, UpdateItemRequest request, Long userId);
}
