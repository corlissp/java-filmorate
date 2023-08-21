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
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> likes;
    private Set<Genre> genre;
    private Mpa mpa;

    public Set<Integer> getLikes() {
        if (likes == null)
            likes = new HashSet<>();
        return likes;
    }
}
