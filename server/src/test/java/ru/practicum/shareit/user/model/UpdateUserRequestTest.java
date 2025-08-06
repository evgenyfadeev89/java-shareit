package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UpdateUserRequestTest {
    @Test
    void testUpdateUserRequestConstructorAndGetters() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");

        assertEquals("Иван Иванов", updateUserRequest.getName());
        assertEquals("ivan.ivanov@example.com", updateUserRequest.getEmail());
    }

    @Test
    void testSetters() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        updateUserRequest.setName("Иван Иванов");
        updateUserRequest.setEmail("ivan.ivanov@example.com");

        assertEquals("Иван Иванов", updateUserRequest.getName());
        assertEquals("ivan.ivanov@example.com", updateUserRequest.getEmail());
    }

    @Test
    void testEquals() {
        UpdateUserRequest updateUserRequest1 = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        UpdateUserRequest updateUserRequest2 = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        UpdateUserRequest updateUserRequest3 = new UpdateUserRequest("Петр Петров", "petr.petrov@example.com");

        assertEquals(updateUserRequest1, updateUserRequest2);
        assertNotEquals(updateUserRequest1, updateUserRequest3);
    }

    @Test
    void testHashCode() {
        UpdateUserRequest updateUserRequest1 = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        UpdateUserRequest updateUserRequest2 = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");

        assertEquals(updateUserRequest1.hashCode(), updateUserRequest2.hashCode());
    }

    @Test
    void testToString() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        String expected = "UpdateUserRequest(name=Иван Иванов, email=ivan.ivanov@example.com)";

        assertEquals(expected, updateUserRequest.toString());
    }

    @Test
    void testEqualsWithNull() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        assertNotEquals(updateUserRequest, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        assertNotEquals(updateUserRequest, "some string");
    }

    @Test
    void testEqualsWithIdenticalData() {
        UpdateUserRequest updateUserRequest1 = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        UpdateUserRequest updateUserRequest2 = new UpdateUserRequest("Иван Иванов", "ivan.ivanov@example.com");

        assertEquals(updateUserRequest1, updateUserRequest2);
    }
}
