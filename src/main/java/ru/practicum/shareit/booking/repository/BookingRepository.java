package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds ORDER BY b.item.id, b.start DESC")
    List<Booking> findByItemIdIn(@Param("itemIds") List<Long> itemIds);

    boolean existsByBookerIdAndItemIdAndEndBefore(Long userId, Long itemId, LocalDateTime now);

    List<Booking> findByItemId(Long itemId);
}
