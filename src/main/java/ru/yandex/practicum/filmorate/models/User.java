package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Min Danil 06.07.2023
 */

@Data
@Builder
@Valid
public class User {
    @PositiveOrZero
    private int id;
    @Email(message = "Неверный email.")
    @NotNull
    private String email;
    @NotNull
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends;

    public Set<Integer> getFriends() {
        if (friends == null)
            friends = new HashSet<>();
        return friends;
    }
}
