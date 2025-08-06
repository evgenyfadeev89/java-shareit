package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.NewUserRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = UserMapper.INSTANCE;
    }

    @Test
    void toUserDtoShouldMapUserToUserDto() {
        User user = new User(1L, "John Doe", "john.doe@example.com");

        UserDto dto = userMapper.toUserDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    void toUserShouldMapNewUserRequestToUser() {
        NewUserRequest request = new NewUserRequest("Jane Doe", "jane.doe@example.com");

        User user = userMapper.toUser(request);

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals(request.getName(), user.getName());
        assertEquals(request.getEmail(), user.getEmail());
    }

    @Test
    void toUserShouldHandleNullNewUserRequest() {
        User user = userMapper.toUser(null);
        assertNull(user);
    }

    @Test
    void toUserDtoShouldHandleNullUser() {
        UserDto dto = userMapper.toUserDto(null);
        assertNull(dto);
    }
}
