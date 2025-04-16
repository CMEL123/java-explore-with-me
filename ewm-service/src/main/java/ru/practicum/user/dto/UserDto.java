package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Size(min = 2, max = 250)
    String name;

    @NotBlank(message = "Field: email. Error: must not be blank. Value: null")
    @Size(min = 6, max = 254)
    @Email(message = "не валидный email")
    String email;
}
