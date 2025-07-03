package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NewItemRequest;
import ru.practicum.shareit.item.model.UpdateItemRequest;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner());
        itemDto.setRequest(item.getRequest() != null ? item.getRequest() : null);

        return itemDto;
    }

    public static Item toItem(NewItemRequest itemRequest) {
        Item item = new Item();

        item.setName(itemRequest.getName());
        item.setDescription(itemRequest.getDescription());
        item.setAvailable(itemRequest.getAvailable());
        item.setOwner(itemRequest.getOwner());
        item.setRequest(itemRequest.getRequest() != null ? itemRequest.getRequest() : null);

        return item;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
        if (request.hasValidDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasValidName()) {
            item.setName(request.getName());
        }
        if (request.hasValidAvailable()) {
            item.setAvailable(request.getAvailable());
        }

        return item;
    }
}
