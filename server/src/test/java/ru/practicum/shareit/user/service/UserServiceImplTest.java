package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.NewUserRequest;
import ru.practicum.shareit.user.model.UpdateUserRequest;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private UserDto userDto;
    private NewUserRequest newUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(null, "Test User", "test@example.com");
        newUserRequest = new NewUserRequest("TestNew User", "testNew@example.com");
        updateUserRequest = new UpdateUserRequest("TestUpd User", "testUpd@example.com");
    }

    @Test
    void createUser() {
        UserDto createdUser = userService.create(newUserRequest);
        assertNotNull(createdUser.getId());
        assertEquals(newUserRequest.getName(), createdUser.getName());
        assertEquals(newUserRequest.getEmail(), createdUser.getEmail());
    }

    @Test
    void updateUser() {
        UserDto createdUser = userService.create(newUserRequest);
        UserDto updatedUser = userService.update(createdUser.getId(), updateUserRequest);
        createdUser.setName(updateUserRequest.getName());
        assertEquals(createdUser.getName(), updatedUser.getName());
    }

    @Test
    void updateUserWithNonExistentId() {
        assertThrows(NotFoundException.class, () -> userService.update(999L, updateUserRequest));
    }

    @Test
    void getUserById() {
        UserDto createdUser = userService.create(newUserRequest);
        UserDto foundUser = userService.getUserById(createdUser.getId());
        assertEquals(createdUser.getId(), foundUser.getId());
    }

    @Test
    void getUserByIdNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void findAll() {
        userService.create(newUserRequest);
        assertFalse(userService.findAll().isEmpty());
    }

    @Test
    void deleteUserById() {
        UserDto createdUser = userService.create(newUserRequest);
        userService.deleteUserById(createdUser.getId());
        assertThrows(NotFoundException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    void deleteUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.deleteUserById(999L));
    }
}