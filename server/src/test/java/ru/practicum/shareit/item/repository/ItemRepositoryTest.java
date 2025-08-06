package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    private User owner;
    private User otherUser;
    private Request request1;
    private Request request2;
    private Item item1;
    private Item item2;
    private Item item3;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner User", "owner@example.com");
        owner = userRepository.save(owner);

        otherUser = new User(null, "Other User", "other@example.com");
        otherUser = userRepository.save(otherUser);

        request1 = new Request(null, "Request 1", owner, LocalDateTime.now());
        request1 = requestRepository.save(request1);

        request2 = new Request(null, "Request 2", otherUser, LocalDateTime.now().plusDays(1));
        request2 = requestRepository.save(request2);

        item1 = new Item(null, "Drill", "Electric drill", true, owner, null);
        item1 = itemRepository.save(item1);

        item2 = new Item(null, "Screwdriver", "Flathead screwdriver", true, owner, request1);
        item2 = itemRepository.save(item2);

        item3 = new Item(null, "Hammer", "Heavy hammer", false, otherUser, request2);
        item3 = itemRepository.save(item3);
    }

    @Test
    void findByOwnerIdShouldReturnAllItemsForOwner() {
        List<Item> items = itemRepository.findByOwnerId(owner.getId());
        assertNotNull(items);
        assertEquals(2, items.size());
        assertTrue(items.stream().allMatch(i -> i.getOwner().getId().equals(owner.getId())));
    }

    @Test
    void findByNameShouldFindItemsByNameOrDescriptionIgnoreCase() {
        List<Item> itemsByName = itemRepository.findByName("drill");
        assertFalse(itemsByName.isEmpty());
        assertTrue(itemsByName.stream().anyMatch(i -> i.getName().equalsIgnoreCase("Drill")));

        List<Item> itemsByDescription = itemRepository.findByName("flathead");
        assertFalse(itemsByDescription.isEmpty());
        assertTrue(itemsByDescription.stream().anyMatch(i -> i.getDescription().equalsIgnoreCase("Flathead screwdriver")));

        List<Item> itemsByPartial = itemRepository.findByName("HAM");
        assertFalse(itemsByPartial.isEmpty());
        assertTrue(itemsByPartial.stream().anyMatch(i -> i.getName().equalsIgnoreCase("Hammer")));
    }

    @Test
    void existsByOwnerIdShouldReturnTrueWhenItemsExist() {
        assertTrue(itemRepository.existsByOwnerId(owner.getId()));
    }

    @Test
    void existsByOwnerIdShouldReturnFalseWhenItemsDoNotExist() {
        assertFalse(itemRepository.existsByOwnerId(9999L));
    }

    @Test
    void findByRequestIdShouldReturnItemsLinkedToRequest() {
        List<Item> items = itemRepository.findByRequestId(request1.getId());
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item2.getId(), items.get(0).getId());
    }

    @Test
    void findByRequestIdShouldReturnEmptyListIfNoItemsForRequest() {
        List<Item> items = itemRepository.findByRequestId(9999L);
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }
}
