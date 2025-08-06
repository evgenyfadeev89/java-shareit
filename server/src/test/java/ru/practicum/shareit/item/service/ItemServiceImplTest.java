package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.AllItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NewCommentRequest;
import ru.practicum.shareit.item.model.NewItem;
import ru.practicum.shareit.item.model.UpdateItem;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceImplTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User owner;
    private User booker;
    private Item item;
    private NewItem newItem;
    private UpdateItem updateItem;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner User", "owner@example.com");
        owner = userRepository.save(owner);

        booker = new User(null, "Booker", "booker@example.com");
        booker = userRepository.save(booker);

        newItem = new NewItem("Test Item",
                "Description",
                true,
                null,
                null);

        item = new Item(null, "Item Name", "Item Description", true, owner, null);
        itemRepository.save(item);
    }

    @Test
    void create() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());
        assertNotNull(createdItem.getId());
        assertEquals(newItem.getName(), createdItem.getName());
    }

    @Test
    void findAllShouldSetLastAndNextBooking() {
        Booking pastBooking = new Booking();
        pastBooking.setItem(item);
        pastBooking.setBooker(booker);
        pastBooking.setStart(LocalDateTime.now().minusDays(5));
        pastBooking.setEnd(LocalDateTime.now().minusDays(2));
        bookingRepository.save(pastBooking);

        Booking futureBooking = new Booking();
        futureBooking.setItem(item);
        futureBooking.setBooker(booker);
        futureBooking.setStart(LocalDateTime.now().plusDays(3));
        futureBooking.setEnd(LocalDateTime.now().plusDays(5));
        bookingRepository.save(futureBooking);

        List<AllItemDto> items = itemService.findAll(owner.getId());

        assertFalse(items.isEmpty());

        AllItemDto returnedItem = items.get(0);

        assertNotNull(returnedItem.getLastBooking());
        assertEquals(pastBooking.getEnd(), returnedItem.getLastBooking());

        assertNotNull(returnedItem.getNextBooking());
        assertEquals(futureBooking.getStart(), returnedItem.getNextBooking());
    }

    @Test
    void update() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());
        UpdateItem updateItem = new UpdateItem("Updated Item", "Updated Description", false);
        ItemDto updatedItem = itemService.update(createdItem.getId(), updateItem, owner.getId());
        assertEquals(updateItem.getName(), updatedItem.getName());
        assertEquals(updateItem.getDescription(), updatedItem.getDescription());
        assertFalse(updatedItem.getAvailable());
    }

    @Test
    void getItemById() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());
        AllItemDto foundItem = itemService.getItemById(createdItem.getId(), owner.getId());
        assertEquals(createdItem.getId(), foundItem.getId());
    }

    @Test
    void findAll() {
        itemService.create(newItem, owner.getId());
        List<AllItemDto> items = itemService.findAll(owner.getId());
        assertFalse(items.isEmpty());
    }

    @Test
    void getItemByName() {
        itemService.create(newItem, owner.getId());
        List<ItemDto> items = itemService.getItemByName("Test");
        assertFalse(items.isEmpty());
    }

    @Test
    void addComment() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());

        Booking booking = new Booking();
        booking.setItem(itemRepository.findById(createdItem.getId()).orElseThrow());
        booking.setBooker(owner);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);

        NewCommentRequest newCommentRequest = new NewCommentRequest("Great item!");
        CommentDto commentDto = itemService.addComment(createdItem.getId(), newCommentRequest, owner.getId());

        assertNotNull(commentDto.getId());
        assertEquals("Great item!", commentDto.getText());
    }

    @Test
    void createItemWithNonExistentUser() {
        assertThrows(NotFoundException.class, () -> itemService.create(newItem, 999L));
    }

    @Test
    void createItemWithNullUser() {
        assertThrows(ConditionsNotMetException.class, () -> itemService.create(newItem, null));
    }

    @Test
    void createItemWithNullItemName() {
        newItem.setName(null);
        assertThrows(ConditionsNotMetException.class, () -> itemService.create(newItem, null));
    }

    @Test
    void createItemWithBlankItemName() {
        newItem.setName("");
        assertThrows(ConditionsNotMetException.class, () -> itemService.create(newItem, null));
    }

    @Test
    void createItemWithNullItemDescription() {
        newItem.setDescription(null);
        assertThrows(ConditionsNotMetException.class, () -> itemService.create(newItem, null));
    }

    @Test
    void createItemWithBlankItemDescription() {
        newItem.setDescription("");
        assertThrows(ConditionsNotMetException.class, () -> itemService.create(newItem, null));
    }

    @Test
    void createItemWithNullItemAvailable() {
        newItem.setAvailable(null);
        assertThrows(ConditionsNotMetException.class, () -> itemService.create(newItem, null));
    }

    @Test
    void createItemWithNotValidItemRequest() {
        newItem.setRequestId(999L);
        assertThrows(ConditionsNotMetException.class, () -> itemService.create(newItem, null));
    }

    @Test
    void updateItemWithNonExistentItem() {
        UpdateItem updateItem = new UpdateItem("Updated Item", "Updated Description", false);
        assertThrows(NotFoundException.class, () -> itemService.update(999L, updateItem, owner.getId()));
    }

    @Test
    void updateItemWithNotOwnerItem() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());
        User notOwner = userRepository.save(new User(null, "notOwner User", "owner2@example.com"));
        UpdateItem updateItem = new UpdateItem("Updated Item", "Updated Description", false);
        assertThrows(ForbiddenException.class, () -> itemService.update(createdItem.getId(), updateItem, notOwner.getId()));
    }

    @Test
    void updateItemWithNullUser() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());
        UpdateItem updateItem = new UpdateItem("Updated Item", "Updated Description", false);
        assertThrows(ConditionsNotMetException.class, () ->
                itemService.update(createdItem.getId(), updateItem, null));
    }

    @Test
    void getItemByIdWithNonExistentItem() {
        assertThrows(NotFoundException.class, () -> itemService.getItemById(999L, owner.getId()));
    }

    @Test
    void getItemsByNonExistentUserId() {
        assertThrows(NotFoundException.class, () -> itemService.getItemById(888L, 999L));
    }

    @Test
    void getItemsByNullUserId() {
        assertThrows(ConditionsNotMetException.class, () -> itemService.getItemById(888L, null));
    }

    @Test
    void searchItemsWithEmptyText() {
        assertThrows(ConditionsNotMetException.class, () -> itemService.getItemByName(""));
    }

    @Test
    void addCommentWithNonExistentUser() {
        assertThrows(NotFoundException.class, () -> itemService.addComment(1L,
                new NewCommentRequest("Nice item"),
                999L));
    }

    @Test
    void addCommentWithNonExistentItem() {
        assertThrows(NotFoundException.class, () -> itemService.addComment(999L,
                new NewCommentRequest("Nice item"),
                1L));
    }

    @Test
    void addCommentWithoutCompletedBooking() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());

        Booking booking = new Booking();
        booking.setItem(itemRepository.findById(createdItem.getId()).orElseThrow());
        booking.setBooker(owner);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);

        NewCommentRequest newCommentRequest = new NewCommentRequest("Great item!");
        CommentDto commentDto = itemService.addComment(createdItem.getId(), newCommentRequest, owner.getId());

        assertNotNull(commentDto.getId());
        assertEquals("Great item!", commentDto.getText());
    }

    @Test
    void getItemByIdWhenUserIsNotOwner() {
        ItemDto createdItem = itemService.create(newItem, owner.getId());
        User anotherUser = new User(null, "Another User", "another@example.com");
        anotherUser = userRepository.save(anotherUser);

        AllItemDto foundItem = itemService.getItemById(createdItem.getId(), anotherUser.getId());
        assertEquals(createdItem.getId(), foundItem.getId());
        assertNull(foundItem.getLastBooking());
        assertNull(foundItem.getNextBooking());
    }
}