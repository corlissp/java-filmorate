package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Min Danil 12.07.2023
 */

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private static final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilmStorage(Film film) {
        films.put(film.getId(), film);
        log.info("INFO: Фильм с id = {} сохранён.", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilmsStorage() {
        List<Film> filmsList = new ArrayList<>();
        for (Integer key : films.keySet())
            filmsList.add(films.get(key));
        log.info("INFO: Все фильмы получены.");
        return filmsList;
    }

    @Override
    public Film updateFilmStorage(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("INFO: Фильм с id = {} обновлён.", film.getId());
        } else {
            log.error("ERROR: Фильм с id = {} не найден.", film.getId());
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден.");
        }
        return film;
    }

    @Override
    public Film getFilmByIdStorage(int id) {
        if (!films.containsKey(id)) {
            log.error("ERROR: Фильм с id = {} не найден.", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден.");
        }
        return films.get(id);
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.addLike(userId);
        updateFilmStorage(film);
        return true;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.deleteLike(userId);
        updateFilmStorage(film);
        return true;
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        log.info("INFO: Получение общих фильмов пользователей с id = " + userId + " и с id = " + friendId + ".");
        return getCommonFilms(userId, friendId);
    }

    private static class IdGenerator {
        private static int id = 1;

        private static int getFreeId() {
            while (films.containsKey(id))
                id++;
            return id;
        }
    }
}
