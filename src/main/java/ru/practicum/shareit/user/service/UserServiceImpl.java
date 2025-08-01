package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.NewUserRequest;
import ru.practicum.shareit.user.model.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }


    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }


    public UserDto create(NewUserRequest request) {
        if (!request.hasValidName()) {
            throw new ConditionsNotMetException("Имя должно быть указано");
        }

        if (!request.hasValidEmail()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        Optional<User> alreadyExistEmailUser = userRepository.findByEmail(request.getEmail());
        if (alreadyExistEmailUser.isPresent()) {
            throw new DuplicatedDataException("Данный имейл уже используется");
        }

        User user = userMapper.toUser(request);
        user = userRepository.save(user);

        return userMapper.toUserDto(user);
    }


    public UserDto update(Long userId, UpdateUserRequest request) {
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        userRepository.findByEmail(request.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(userId)) {
                        throw new DuplicatedDataException("Такой email уже используется");
                    }
                });

        if (request.hasValidName()) {
            updatedUser.setName(request.getName());
        }
        if (request.hasValidEmail()) {
            updatedUser.setEmail(request.getEmail());
        }

        return userMapper.toUserDto(userRepository.save(updatedUser));
    }


    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}

