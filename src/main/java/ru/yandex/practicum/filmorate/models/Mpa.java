package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Min Danil 21.08.2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    private int id;
    private String name;
    private String description;
}
