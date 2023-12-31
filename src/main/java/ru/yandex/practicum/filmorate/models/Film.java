package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

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
    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов. ")
    private String description;
    @PastOrPresent(message = "Дата релиза не может быть в будущем. ")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной. ")
    private int duration;
    private int rate;
    private Mpa mpa;
    private List<Genre> genres;
    private List<Integer> likes;
    private Set<Director> directors;

    public void addLike(Integer userId) {
        likes.add(userId);
    }

    public void deleteLike(Integer userId) {
        likes.remove(userId);
    }

    public List<Genre> getGenres() {
        if (genres == null)
            genres = new ArrayList<>();
        return genres;
    }

    public Set<Director> getDirectors() {
        if (directors == null)
            directors = new HashSet<>();
        return directors;
    }

    public List<Integer> getLikes() {
        if (likes == null)
            likes = new ArrayList<>();
        return likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && rate == film.rate && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(mpa, film.mpa) && Objects.equals(genres, film.genres) && Objects.equals(likes, film.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, rate, mpa, genres, likes);
    }
}
