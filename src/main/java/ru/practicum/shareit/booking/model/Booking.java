package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_items_id"), nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", foreignKey = @ForeignKey(name = "fk_booker_id"), nullable = false)
    private User booker;

    @Enumerated(EnumType.STRING)
    private Status status;
}
