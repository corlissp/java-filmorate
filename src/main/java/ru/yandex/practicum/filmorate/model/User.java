package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDate;

/**
 * @author Min Danil 06.07.2023
 */

@Data
public class User {
    int id;
    @Email
    String email;
    @NotBlank
    String login;
    @NotNull
    String name;
    @NotBlank
    LocalDate birthday;
}
