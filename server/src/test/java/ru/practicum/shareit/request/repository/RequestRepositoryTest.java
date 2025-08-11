package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Request request1;
    private Request request2;
    private Request request3;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "User1", "user1@example.com");
        user1 = userRepository.save(user1);

        user2 = new User(null, "User2", "user2@example.com");
        user2 = userRepository.save(user2);

        request1 = new Request(null, "Request 1", user1, LocalDateTime.now().minusDays(2));
        request2 = new Request(null, "Request 2", user1, LocalDateTime.now().minusDays(1));
        request3 = new Request(null, "Request 3", user2, LocalDateTime.now());

        request1 = requestRepository.save(request1);
        request2 = requestRepository.save(request2);
        request3 = requestRepository.save(request3);
    }

    @Test
    void findByRequestorIdOrderByCreatedDescShouldReturnRequestsForUserOrderedDesc() {
        List<Request> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(user1.getId());

        assertNotNull(requests);
        assertEquals(2, requests.size());

        assertTrue(requests.get(0).getCreated().isAfter(requests.get(1).getCreated()) ||
                requests.get(0).getCreated().isEqual(requests.get(1).getCreated()));

        assertTrue(requests.stream().allMatch(r -> r.getRequestor().getId().equals(user1.getId())));
    }

    @Test
    void findByRequestorIdNotOrderByCreatedDescShouldReturnRequestsNotForUserOrderedDesc() {
        List<Request> requests = requestRepository.findByRequestorIdNotOrderByCreatedDesc(user1.getId());

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals(request3.getId(), requests.get(0).getId());
        assertEquals(user2.getId(), requests.get(0).getRequestor().getId());
    }

    @Test
    void findByRequestorIdNotOrderByCreatedDescWhenNoOtherRequestsShouldReturnEmpty() {
        List<Request> requests = requestRepository.findByRequestorIdNotOrderByCreatedDesc(999999L);
        assertNotNull(requests);
        assertEquals(3, requests.size());
    }

    @Test
    void findByRequestorIdOrderByCreatedDescWhenNoRequestsShouldReturnEmpty() {
        List<Request> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(999999L);
        assertNotNull(requests);
        assertTrue(requests.isEmpty());
    }
}