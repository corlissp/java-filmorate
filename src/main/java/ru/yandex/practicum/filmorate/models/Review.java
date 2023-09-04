package ru.yandex.practicum.filmorate.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Review {

    @NotBlank(message = "Содержание ревью не должно быть пустым")
    @Size(message = "Содержание ревью не должно прывышать 1000 символов", max = 1000)
    private final String content;
    @NotNull(message = "UserId не должно быть null")
    private final Integer userId;
    @NotNull(message = "FilmId не должно быть null")
    private final Integer filmId;
    @NotNull(message = "isPositive не должно быть null")
    private final boolean isPositive;
    private final int useful;
    private int reviewId;

    public boolean getIsPositive() {
        return this.isPositive;
    }
}
