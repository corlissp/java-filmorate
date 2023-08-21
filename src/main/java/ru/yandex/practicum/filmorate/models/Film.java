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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private Mpa mpa;
    private List<Genre> genres;
    private List<Integer> likes;

    public boolean addLike(Integer userId) {
        return likes.add(userId);
    }

    public boolean deleteLike(Integer userId) {
        return likes.remove(userId);
    }

    public List<Integer> getLikes() {
        if (likes == null)
            likes = new ArrayList<>();
        return likes;
    }
}
