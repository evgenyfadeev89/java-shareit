package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testHasValidName() {
        NewUserRequest withValidName = new NewUserRequest("Иван Иванов", "valid@example.com");
        assertTrue(withValidName.hasValidName());

        NewUserRequest withNullName = new NewUserRequest(null, "valid@example.com");
        assertFalse(withNullName.hasValidName());

        NewUserRequest withEmptyName = new NewUserRequest("", "valid@example.com");
        assertFalse(withEmptyName.hasValidName());

        NewUserRequest withBlankName = new NewUserRequest("    ", "valid@example.com");
        assertFalse(withBlankName.hasValidName());
    }

    @Test
    void testHasValidEmail() {
        NewUserRequest withValidEmail = new NewUserRequest("Иван Иванов", "ivan.ivanov@example.com");
        assertTrue(withValidEmail.hasValidEmail());

        NewUserRequest withNullEmail = new NewUserRequest("Иван Иванов", null);
        assertFalse(withNullEmail.hasValidEmail());

        NewUserRequest withEmptyEmail = new NewUserRequest("Иван Иванов", "");
        assertFalse(withEmptyEmail.hasValidEmail());

        NewUserRequest withBlankEmail = new NewUserRequest("Иван Иванов", "   ");
        assertFalse(withBlankEmail.hasValidEmail());

        NewUserRequest withoutAtEmail = new NewUserRequest("Иван Иванов", "ivan.ivanovexample.com");
        assertFalse(withoutAtEmail.hasValidEmail());
    }

}
