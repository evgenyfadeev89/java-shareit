package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewCommentRequest {

    @NotBlank
    private String text;

    public boolean hasValidtext() {
        return !(text == null || text.isBlank());
    }
}
