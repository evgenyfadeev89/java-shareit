package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.NewUserRequest;
import ru.practicum.shareit.user.model.UpdateUserRequest;

import java.util.List;


public interface UserService {

    List<UserDto> findAll();

    UserDto getUserById(Long userId);

    UserDto create(NewUserRequest request);

    UserDto update(Long userId, UpdateUserRequest request);

    void deleteUserById(Long userId);
}
