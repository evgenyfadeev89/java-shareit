package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {

    private String name;

    @Email
    private String email;

    public boolean hasValidName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasValidEmail() {
        return !(email == null || email.isBlank() || !email.contains("@"));
    }
}
