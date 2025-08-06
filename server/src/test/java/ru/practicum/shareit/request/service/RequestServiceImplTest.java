package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.PersonalRequestDto;
import ru.practicum.shareit.request.dto.PublicRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.NewRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RequestServiceImplTest {

    @Autowired
    private RequestServiceImpl requestService;

    @Autowired
    private UserRepository userRepository;

    private User requestor;
    private NewRequest newRequest;

    @BeforeEach
    void setUp() {
        requestor = new User(null, "Test User", "test@example.com");
        requestor = userRepository.save(requestor);
        newRequest = new NewRequest("Test Request", requestor.getId(), null);
    }

    @Test
    void create() {
        RequestDto createdRequest = requestService.create(newRequest, requestor.getId(), LocalDateTime.now());
        assertNotNull(createdRequest.getId());
        assertEquals(createdRequest.getDescription(), newRequest.getDescription());
    }

    @Test
    void createNotValidDescription() {
        newRequest.setDescription(null);
        assertThrows(ConditionsNotMetException.class, () ->
                requestService.create(newRequest, requestor.getId(), LocalDateTime.now()));
    }

    @Test
    void createNotValidRequestor() {
        assertThrows(NotFoundException.class, () ->
                requestService.create(newRequest, 999L, LocalDateTime.now()));
    }

    @Test
    void findAllPersonal() {
        requestService.create(newRequest, requestor.getId(), LocalDateTime.now());
        List<PersonalRequestDto> requests = requestService.findAllPersonal(requestor.getId());
        assertFalse(requests.isEmpty());
    }

    @Test
    void findAll() {
        requestService.create(newRequest, requestor.getId(), LocalDateTime.now());
        List<PublicRequestDto> requests = requestService.findAll(requestor.getId());
        assertTrue(requests.isEmpty());
    }

    @Test
    void getRequestById() {
        RequestDto createdRequest = requestService.create(newRequest, requestor.getId(), LocalDateTime.now());
        PersonalRequestDto foundRequest = requestService.getRequestById(createdRequest.getId(), requestor.getId());
        assertEquals(createdRequest.getId(), foundRequest.getId());
    }

    @Test
    void getRequestByIdNotFound() {
        assertThrows(NotFoundException.class, () -> requestService.getRequestById(requestor.getId(), 999L));
    }
}