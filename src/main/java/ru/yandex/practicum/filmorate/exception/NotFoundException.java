package ru.yandex.practicum.filmorate.exception;

/**
 * @author Min Danil 14.07.2023
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
