package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NewItemTest {

    @Test
    void testNewItemConstructorAndGetters() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Request request = new Request();
        NewItem newItem = new NewItem("NewItem Name",
                "NewItem Description",
                true,
                owner.getId(),
                request.getId());

        assertEquals("NewItem Name", newItem.getName());
        assertEquals("NewItem Description", newItem.getDescription());
        assertTrue(newItem.getAvailable());
        assertEquals(owner.getId(), newItem.getOwner());
        assertEquals(request.getId(), newItem.getRequestId());
    }

    @Test
    void testSetters() {
        User owner = new User(1L, "Owner", "owner@example.com");
        Request request = new Request();
        NewItem newItem = new NewItem();

        newItem.setName("NewItem Name");
        newItem.setDescription("NewItem Description");
        newItem.setAvailable(false);
        newItem.setOwner(owner.getId());
        newItem.setRequestId(request.getId());

        assertEquals("NewItem Name", newItem.getName());
        assertEquals("NewItem Description", newItem.getDescription());
        assertFalse(newItem.getAvailable());
        assertEquals(owner.getId(), newItem.getOwner());
        assertEquals(request.getId(), newItem.getRequestId());
    }

    @Test
    void testEquals() {
        User owner = new User(1L,
                "Owner",
                "owner@example.com");
        NewItem newItem1 = new NewItem("Item Name",
                "Item Description",
                true,
                owner.getId(),
                null);
        NewItem newItem2 = new NewItem("Item Name",
                "Item Description",
                true,
                owner.getId(),
                null);
        NewItem newItem3 = new NewItem("Another Item",
                "Another Description",
                true,
                owner.getId(),
                null);

        assertEquals(newItem1, newItem2);
        assertNotEquals(newItem1, newItem3);
    }

    @Test
    void testHashCode() {
        User owner = new User(1L, "Owner", "owner@example.com");
        NewItem newItem1 = new NewItem("NewItem Name",
                "NewItem Description",
                true,
                owner.getId(),
                null);
        NewItem newItem2 = new NewItem("NewItem Name",
                "NewItem Description",
                true,
                owner.getId(),
                null);

        assertEquals(newItem1.hashCode(), newItem2.hashCode());
    }

    @Test
    void testToString() {
        User owner = new User(1L, "Owner", "owner@example.com");
        NewItem newItem = new NewItem("NewItem Name",
                "NewItem Description",
                true,
                owner.getId(),
                null);
        String expected = "NewItem(name=NewItem Name, " +
                "description=NewItem Description, " +
                "available=true, " +
                "owner=1, " +
                "requestId=null)";

        assertEquals(expected, newItem.toString());
    }

    @Test
    void testEqualsWithNull() {
        User owner = new User(1L, "Owner", "owner@example.com");
        NewItem newItem = new NewItem("NewItem Name",
                "NewItem Description",
                true,
                owner.getId(),
                null);

        assertNotEquals(newItem, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        User owner = new User(1L, "Owner", "owner@example.com");
        NewItem newItem = new NewItem("NewItem Name",
                "NewItem Description",
                true,
                owner.getId(),
                null);
        assertNotEquals(newItem, new Object());
    }
}
