package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {

    String name;

    @Email
    String email;

    public boolean hasValidName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasValidEmail() {
        return !(email == null || email.isBlank() || !email.contains("@"));
    }
}
