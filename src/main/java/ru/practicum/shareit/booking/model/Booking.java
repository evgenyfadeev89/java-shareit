package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;


@Data
public class Booking {
    @NotBlank
    private Long id;

    @PastOrPresent(message = "Дата не может быть в прошлом")
    private LocalDate start;

    @FutureOrPresent(message = "Дата не может быть в прошлом")
    private LocalDate end;

    private Item item;

    private User booker;

    private Status status;
}
