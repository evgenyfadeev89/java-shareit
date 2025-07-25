package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId);

    @Query("""
                select i 
                from Item as i
                where lower(i.name) like lower(concat('%', :text, '%'))
                or lower(description) like lower(concat('%', :text, '%'))
            """)
    List<Item> findByName(@Param("text") String text);

    boolean existsByOwnerId(Long userId);
}
