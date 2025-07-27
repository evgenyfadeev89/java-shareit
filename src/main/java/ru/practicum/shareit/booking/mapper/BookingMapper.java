package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.NewBooking;


@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "item.owner.id", target = "item.owner")
    @Mapping(source = "item.request.id", target = "item.request")
    @Mapping(source = "booker", target = "booker")
    BookingDto toBookingDto(Booking booking);

    @Mapping(source = "booker", target = "booker.id")
    @Mapping(source = "itemId", target = "item.id")
    Booking toBooking(NewBooking newBooking);

    @Mapping(source = "item.owner.id", target = "item.owner")
    @Mapping(source = "item.request.id", target = "item.request")
    BookingDto toOwnerItemsBookingDto(Booking booking);
}
