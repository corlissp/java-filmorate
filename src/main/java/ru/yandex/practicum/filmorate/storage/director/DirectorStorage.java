package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.models.Director;

import java.util.List;

/**
 * @author Min Danil 29.08.2023
 */
public interface DirectorStorage {
    List<Director> getAllDirectors();

    Director getDirectorById(int id);

    Director addDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(int id);

    List<Director> getDirectorsByFilm(int filmId);

}
