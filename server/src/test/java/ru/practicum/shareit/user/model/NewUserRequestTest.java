package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NewUserRequestTest {

    @Test
    void testNewUserRequestConstructorAndGetters() {
        NewUserRequest newUserRequest = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");

        assertEquals("Иван Иванов", newUserRequest.getName());
        assertEquals("ivan.ivanov@example.com", newUserRequest.getEmail());
    }

    @Test
    void testSetters() {
        NewUserRequest newUserRequest = new NewUserRequest();

        newUserRequest.setName("Иван Иванов");
        newUserRequest.setEmail("ivan.ivanov@example.com");

        assertEquals("Иван Иванов", newUserRequest.getName());
        assertEquals("ivan.ivanov@example.com", newUserRequest.getEmail());
    }

    @Test
    void testEquals() {
        NewUserRequest newUserRequest1 = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        NewUserRequest newUserRequest2 = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        NewUserRequest newUserRequest3 = new NewUserRequest("Петр Петров", "petr.petrov@example.com");

        assertEquals(newUserRequest1, newUserRequest2);
        assertNotEquals(newUserRequest1, newUserRequest3);
    }

    @Test
    void testHashCode() {
        NewUserRequest newUserRequest1 = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        NewUserRequest newUserRequest2 = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");

        assertEquals(newUserRequest1.hashCode(), newUserRequest2.hashCode());
    }

    @Test
    void testToString() {
        NewUserRequest newUserRequest = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        String expected = "NewUserRequest(name=Иван Иванов, email=ivan.ivanov@example.com)";

        assertEquals(expected, newUserRequest.toString());
    }

    @Test
    void testEqualsWithNull() {
        NewUserRequest newUserRequest = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        assertNotEquals(newUserRequest, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        NewUserRequest newUserRequest = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        assertNotEquals(newUserRequest, "some string");
    }

    @Test
    void testEqualsWithIdenticalData() {
        NewUserRequest newUserRequest1 = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        NewUserRequest newUserRequest2 = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");

        assertEquals(newUserRequest1, newUserRequest2);
    }
}
