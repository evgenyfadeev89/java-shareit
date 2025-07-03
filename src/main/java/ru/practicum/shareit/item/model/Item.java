package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class Item {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Boolean available;

    private Long owner;

    private Long request;
}
