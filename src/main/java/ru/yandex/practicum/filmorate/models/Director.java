package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Min Danil 29.08.2023
 */
@Data
@Builder
@AllArgsConstructor
public class Director {
    private int id;
    @NotBlank(message = "Имя не должно быть пустым")
    private final String name;
}
