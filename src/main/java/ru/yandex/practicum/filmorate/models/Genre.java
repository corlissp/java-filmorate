package ru.yandex.practicum.filmorate.models;

import lombok.Data;

/**
 * @author Min Danil 21.08.2023
 */
@Data
public class Genre {
    private int id;
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
