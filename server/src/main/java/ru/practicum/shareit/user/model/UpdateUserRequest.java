package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
