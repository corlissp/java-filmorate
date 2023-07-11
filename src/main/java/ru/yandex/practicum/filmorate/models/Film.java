package ru.yandex.practicum.filmorate.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Min Danil 06.07.2023
 */

@Data
public class Film {
    private int id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
}
