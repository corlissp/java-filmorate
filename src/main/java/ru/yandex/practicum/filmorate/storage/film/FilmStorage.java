package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

/**
 * @author Min Danil 13.07.2023
 */
public interface FilmStorage {
    Film addFilmStorage(Film film);
    List<Film> getAllFilmsStorage();
    Film updateFilmStorage(Film film);
}
