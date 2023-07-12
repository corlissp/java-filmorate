package ru.yandex.practicum.filmorate.exception;

/**
 * @author Min Danil 06.07.2023
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
