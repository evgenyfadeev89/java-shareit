package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentRequest {

    @NotBlank
    private String text;

    public boolean hasValidtext() {
        return !(text == null || text.isBlank());
    }
}
