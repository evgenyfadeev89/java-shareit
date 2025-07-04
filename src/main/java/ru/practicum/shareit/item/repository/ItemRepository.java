package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();


    private Long getId() {
        return items.keySet().stream().max(Long::compare).orElse(0L) + 1L;
    }


    public Collection<Item> findAll(Long userId) {
        log.info("Вызов показа всех пользователей");
        return items
                .values()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }


    public Optional<Item> findById(long itemId) {
        return Optional.of(items.get(itemId));
    }


    public List<Item> findByName(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        } else {
            return items.values()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(item ->
                            Optional.ofNullable(item.getName()).orElse("").equalsIgnoreCase(text) ||
                                    Optional.ofNullable(item.getDescription()).orElse("").toLowerCase().contains(text.toLowerCase())
                    )
                    .collect(Collectors.toList());
        }
    }


    public Item save(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);

        return item;
    }

    public Item update(Item item) {
        Long id = item.getId();
        Item itm = items.get(id);
        itm.setName(item.getName());
        itm.setDescription(item.getDescription());
        itm.setAvailable(item.getAvailable());
        itm.setOwner(item.getOwner());
        itm.setRequest(item.getRequest() != null ? item.getRequest() : null);

        items.put(id, itm);

        return itm;
    }
}
