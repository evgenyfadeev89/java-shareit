package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Data
public class ItemRequest {
    private Long id;
    @NotBlank
    private String description;
    private User requestor;
    private LocalDateTime created;
}
