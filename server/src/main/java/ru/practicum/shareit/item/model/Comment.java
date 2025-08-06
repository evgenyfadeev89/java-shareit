package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 255)
    private String text;

    @ManyToOne
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_item_id"), nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_author_id"), nullable = false)
    private User author;

    @Transient
    private LocalDateTime created = LocalDateTime.now();
}
