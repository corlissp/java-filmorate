package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Min Danil 06.07.2023
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private List<Integer> friends;

    public boolean addFriend(final Integer id) {
        return friends.add(id);
    }

    public boolean deleteFriend(final Integer id) {
        return friends.remove(id);
    }

    public List<Integer> getFriends() {
        if (friends == null)
            friends = new ArrayList<>();
        return friends;
    }
}
