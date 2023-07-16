package ru.yandex.practicum.filmorate.exception;

/**
 * @author Min Danil 16.07.2023
 */
public class NotFoundForUpdateException extends RuntimeException {
    public NotFoundForUpdateException(String message) {
        super(message);
    }
}
