package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewItemRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
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
