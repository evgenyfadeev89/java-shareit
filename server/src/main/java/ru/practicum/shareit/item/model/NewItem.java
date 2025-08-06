package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewItem {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private Boolean available;
    private Long owner;
    private Long requestId;

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
