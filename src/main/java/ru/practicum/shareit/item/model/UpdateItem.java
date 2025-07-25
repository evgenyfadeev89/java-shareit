package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class UpdateItem {

    private String name;
    private String description;
    private Boolean available;

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
