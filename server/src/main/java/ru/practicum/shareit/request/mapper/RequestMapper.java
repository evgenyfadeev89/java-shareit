package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.PersonalRequestDto;
import ru.practicum.shareit.request.dto.PublicRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.*;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    PersonalRequestDto toPersonalRequestDto(Request request);

    RequestDto toRequestDto(Request request);

    @Mapping(source = "requestor", target = "requestor.id")
    Request toRequest(NewRequest newRequest);

    @Mapping(source = "owner.id", target = "owner")
    AnswerDto toAnswerDto(Item item);

    PublicRequestDto toPublicRequestDto(Request request);
}
