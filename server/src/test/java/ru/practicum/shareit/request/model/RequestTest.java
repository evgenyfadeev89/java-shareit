package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RequestTest {

    @Test
    void testRequestConstructorAndGetters() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        Request request = new Request(1L, "Test Description", requestor, created);

        assertEquals(1L, request.getId());
        assertEquals("Test Description", request.getDescription());
        assertEquals(requestor, request.getRequestor());
        assertEquals(created, request.getCreated());
    }

    @Test
    void testSetters() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        Request request = new Request();

        request.setId(2L);
        request.setDescription("New Description");
        request.setRequestor(requestor);
        request.setCreated(created);

        assertEquals(2L, request.getId());
        assertEquals("New Description", request.getDescription());
        assertEquals(requestor, request.getRequestor());
        assertEquals(created, request.getCreated());
    }

    @Test
    void testEquals() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        Request request1 = new Request(1L, "Test Description", requestor, created);
        Request request2 = new Request(1L, "Test Description", requestor, created);
        Request request3 = new Request(2L, "Another Description", requestor, created);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, null);
        assertNotEquals(request1, new Object());
    }

    @Test
    void testHashCode() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        Request request1 = new Request(1L, "Test Description", requestor, created);
        Request request2 = new Request(1L, "Test Description", requestor, created);
        Request request3 = new Request(2L, "Another Description", requestor, created);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        Request request = new Request(1L, "Test Description", requestor, created);
        String expected = "Request(id=1, description=Test Description, requestor=" + requestor.toString() + ", created=" + created + ")";

        assertEquals(expected, request.toString());
    }

    @Test
    void testEqualsWithDifferentId() {
        User requestor = new User(1L, "Иван Иванов", "ivan.ivanov@example.com");
        LocalDateTime created = LocalDateTime.now();
        Request request1 = new Request(1L, "Test Description", requestor, created);
        Request request2 = new Request(2L, "Test Description", requestor, created);

        assertNotEquals(request1, request2);
    }
}