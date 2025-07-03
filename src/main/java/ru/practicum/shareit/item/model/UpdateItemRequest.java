package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class UpdateItemRequest {

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Long request;

    public boolean hasValidName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasValidDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasValidAvailable() {
        return !(available == null);
    }
}
