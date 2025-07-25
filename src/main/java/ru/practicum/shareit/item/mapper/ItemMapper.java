package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.AllItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NewItem;
import ru.practicum.shareit.item.model.UpdateItem;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;


@Mapper(componentModel = "spring",
        uses = {UserRepository.class, RequestRepository.class})
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "owner.id", target = "owner")
    @Mapping(source = "request.id", target = "request")
    ItemDto toItemDto(Item item);


    @Mapping(source = "owner.id", target = "owner")
    @Mapping(source = "request.id", target = "request")
    AllItemDto toAllItemDto(Item item);


    @Mapping(source = "owner", target = "owner.id")
    @Mapping(target = "id", ignore = true)
    Item toItem(NewItem newItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "request", ignore = true)
    void updateItemFields(UpdateItem updateItem, @MappingTarget Item item);

    @AfterMapping
    default void applyConditionalUpdates(UpdateItem updateItem, @MappingTarget Item item) {
        if (updateItem.hasValidDescription()) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.hasValidName()) {
            item.setName(updateItem.getName());
        }
        if (updateItem.hasValidAvailable()) {
            item.setAvailable(updateItem.getAvailable());
        }
    }
}
