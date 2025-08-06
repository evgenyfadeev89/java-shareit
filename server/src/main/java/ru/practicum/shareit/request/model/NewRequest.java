package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewRequest {

    private String description;
    private Long requestor;
    private LocalDateTime created;

    public boolean hasValidDescription() {
        return !(description == null || description.isBlank());
    }
}
