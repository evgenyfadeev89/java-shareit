package ru.practicum.shareit.item.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NewItemRequest;
import ru.practicum.shareit.item.model.UpdateItemRequest;


@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto toItemDto(Item item);

    Item toItem(NewItemRequest itemRequest);

    @Mapping(target = "id", ignore = true) // Игнорируем поля, которые не должны обновляться
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "request", ignore = true)
    void updateItemFields(UpdateItemRequest request, @MappingTarget Item item);

    @AfterMapping
    default void applyConditionalUpdates(UpdateItemRequest request, @MappingTarget Item item) {
        if (request.hasValidDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasValidName()) {
            item.setName(request.getName());
        }
        if (request.hasValidAvailable()) {
            item.setAvailable(request.getAvailable());
        }
    }
}
