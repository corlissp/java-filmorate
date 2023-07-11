package ru.yandex.practicum.filmorate.models;

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
    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    @NotNull
    private String name;
    private LocalDate birthday;
}
