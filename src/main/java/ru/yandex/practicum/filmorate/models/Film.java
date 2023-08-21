package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getId() == film.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
