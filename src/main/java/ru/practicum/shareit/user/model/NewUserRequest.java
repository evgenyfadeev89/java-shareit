package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewUserRequest {

    @NotBlank
    String name;

    @Email
    @NotBlank
    String email;

    public boolean hasValidName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasValidEmail() {
        return !(email == null || email.isBlank() || !email.contains("@"));
    }
}