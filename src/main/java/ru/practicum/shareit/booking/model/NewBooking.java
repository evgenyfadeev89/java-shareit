package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class NewBooking {

    @NotNull
    private Long itemId;
    private Long booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status = Status.WAITING;
}
