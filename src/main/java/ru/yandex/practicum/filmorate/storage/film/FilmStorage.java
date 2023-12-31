package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

/**
 * @author Min Danil 13.07.2023
 */
public interface FilmStorage {
    Film addFilmStorage(Film film);

    void deleteFilm(int id);

    List<Film> getAllFilmsStorage();

    Film updateFilmStorage(Film film);

    Film getFilmByIdStorage(int id);

    boolean addLike(int filmId, int userId);

    boolean deleteLike(int filmId, int userId);

    List<Film> getFilmsByDirectorIdSortedByYearOrLikes(int id, String sortBy);

    List<Film> getCommonFilms(int userId, int filmId);

    List<Film> getRecommendations(int userId);

    List<Film> searchFilms(String query, String by);
}
