package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.AllItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.NewCommentRequest;
import ru.practicum.shareit.item.model.NewItem;
import ru.practicum.shareit.item.model.UpdateItem;

import java.util.List;


public interface ItemService {

    List<AllItemDto> findAll(Long userId);

    AllItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemByName(String text);

    ItemDto create(NewItem newItem, Long userId);

    ItemDto update(Long itemId, UpdateItem request, Long userId);

    CommentDto addComment(Long itemId, NewCommentRequest commentRequest, Long userId);
}
