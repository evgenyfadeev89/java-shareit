package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(null, "Test User", "test.user@example.com");
        user = userRepository.save(user);
    }

    @Test
    void findByEmailShouldReturnUserWhenEmailExists() {
        Optional<User> foundUser = userRepository.findByEmail("test.user@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void findByEmailShouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertTrue(foundUser.isEmpty());
    }
}
