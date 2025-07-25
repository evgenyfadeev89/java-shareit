package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class OwnerItemsBookingRequest {

    private Item item;
    private LocalDateTime start;
    private LocalDateTime end;
    private User booker;
    private Status status = Status.WAITING;
}
