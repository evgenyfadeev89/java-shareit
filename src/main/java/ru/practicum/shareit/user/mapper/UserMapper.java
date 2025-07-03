package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.NewUserRequest;
import ru.practicum.shareit.user.model.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        return userDto;
    }


    public static User toUser(NewUserRequest request) {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setName(request.getName());

        return user;
    }


    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasValidEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasValidName()) {
            user.setName(request.getName());
        }

        return user;
    }
}
