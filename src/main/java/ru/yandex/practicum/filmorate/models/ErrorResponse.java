package ru.yandex.practicum.filmorate.models;

import lombok.Getter;

/**
 * @author Min Danil 16.07.2023
 */
@Getter
public class ErrorResponse {
    private final String description;

    public ErrorResponse(String description) {
        this.description = description;
    }
}
