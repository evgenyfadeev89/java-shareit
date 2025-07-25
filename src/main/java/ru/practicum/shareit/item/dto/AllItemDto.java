package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AllItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long request;
    private LocalDateTime nextBooking;
    private LocalDateTime lastBooking;
    private List<CommentDto> comments;
}
