package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Entity
@Table(name = "requests")
@Data
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 1024, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id", foreignKey = @ForeignKey(name = "fk_requestor_id"), nullable = false)
    private User requestor;

    @Transient
    private LocalDateTime created;
}
