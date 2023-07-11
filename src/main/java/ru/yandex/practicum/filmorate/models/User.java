package ru.yandex.practicum.filmorate.models;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author Min Danil 06.07.2023
 */

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
