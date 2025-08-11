package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NewRequestTest {

    @Test
    void testNewRequestConstructorAndGetters() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        NewRequest newRequest = new NewRequest("Test Description", requestor.getId(), created);

        assertEquals("Test Description", newRequest.getDescription());
        assertEquals(requestor.getId(), newRequest.getRequestor());
        assertEquals(created, newRequest.getCreated());
    }

    @Test
    void testSetters() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        NewRequest newRequest = new NewRequest();

        newRequest.setDescription("New Description");
        newRequest.setRequestor(requestor.getId());
        newRequest.setCreated(created);

        assertEquals("New Description", newRequest.getDescription());
        assertEquals(requestor.getId(), newRequest.getRequestor());
        assertEquals(created, newRequest.getCreated());
    }

    @Test
    void testEquals() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        NewRequest newRequest1 = new NewRequest("Test Description", requestor.getId(), created);
        NewRequest newRequest2 = new NewRequest("Test Description", requestor.getId(), created);
        NewRequest newRequest3 = new NewRequest("Another Description", requestor.getId(), created);

        assertEquals(newRequest1, newRequest2);
        assertNotEquals(newRequest1, newRequest3);
        assertNotEquals(newRequest1, null);
        assertNotEquals(newRequest1, new Object());
    }

    @Test
    void testHashCode() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        NewRequest newRequest1 = new NewRequest("Test Description", requestor.getId(), created);
        NewRequest newRequest2 = new NewRequest("Test Description", requestor.getId(), created);
        NewRequest newRequest3 = new NewRequest("Another Description", requestor.getId(), created);

        assertEquals(newRequest1.hashCode(), newRequest2.hashCode());
        assertNotEquals(newRequest1.hashCode(), newRequest3.hashCode());
    }

    @Test
    void testToString() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        NewRequest newRequest = new NewRequest("Test Description", requestor.getId(), created);
        String expected = "NewRequest(description=Test Description, " +
                "requestor=" + requestor.getId() +
                ", created=" + created + ")";

        assertEquals(expected, newRequest.toString());
    }

    @Test
    void testEqualsWithDifferentId() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        NewRequest newRequest1 = new NewRequest("Test Description", requestor.getId(), created);
        NewRequest newRequest2 = new NewRequest("Test Description2", requestor.getId(), created);

        assertNotEquals(newRequest1, newRequest2);
    }
}
