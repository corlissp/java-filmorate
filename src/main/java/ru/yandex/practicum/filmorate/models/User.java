package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Min Danil 06.07.2023
 */

@Data
@Builder
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends;

    public void addFriend(int id) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(id);
    }
}
