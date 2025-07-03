package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.NewUserRequest;
import ru.practicum.shareit.user.model.UpdateUserRequest;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody NewUserRequest userRequest) {
        return userService.create(userRequest);
    }

    @PatchMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable("userId") Long userId,
                          @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.update(userId, updateUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
