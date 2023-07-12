package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

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
    private int rate;
}
