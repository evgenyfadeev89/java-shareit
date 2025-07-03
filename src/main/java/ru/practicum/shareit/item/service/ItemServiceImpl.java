package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NewItemRequest;
import ru.practicum.shareit.item.model.UpdateItemRequest;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public List<ItemDto> findAll(Long userId) {
        return itemRepository.findAll(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    @Override
    public ItemDto getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена с ID: " + itemId));
    }


    @Override
    public List<ItemDto> getItemByName(String text) {
        return itemRepository.findByName(text)
                .stream()
                .filter(itm -> itm.getAvailable().equals(true))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    @Override
    public ItemDto create(NewItemRequest request, Long userId) {
        if (userId == null) {
            throw new NotFoundException("ID пользователя не указан");
        }
        if (!request.hasValidName()) {
            throw new ConditionsNotMetException("Имя должно быть указано");
        }
        if (!request.hasValidDescription()) {
            throw new ConditionsNotMetException("Описание должно быть указано");
        }
        if (!request.hasValidAvailable()) {
            throw new ConditionsNotMetException("Статус должен быть указан");
        }
        if (!userRepository.findById(userId).isPresent()) {
            throw new NotFoundException("Такого пользователя не существует");
        }

        request.setOwner(userId);
        Item item = ItemMapper.toItem(request);
        item = itemRepository.save(item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long itemId, UpdateItemRequest request, Long userId) {
        if (!itemRepository.findById(itemId).get().getOwner().equals(userId)) {
            throw new ForbiddenException("Редактировать может только владелец вещи");
        }

        Item updatedItem = itemRepository.findById(itemId)
                .map(item -> ItemMapper.updateItemFields(item, request))
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        updatedItem = itemRepository.update(updatedItem);

        return ItemMapper.toItemDto(updatedItem);
    }
}
