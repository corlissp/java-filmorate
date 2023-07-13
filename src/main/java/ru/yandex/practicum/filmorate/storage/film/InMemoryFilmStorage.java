package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.FilmService.checkValidationFilm;

/**
 * @author Min Danil 12.07.2023
 */

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Map<Integer, Film> films = new HashMap<>();

    public Film addFilmStorage(Film film) {
        checkValidationFilm(film);
        int id = IdGenerator.getFreeId();
        film.setId(id);
        films.put(id, film);
        log.info("INFO: Film is saved.");
        return film;
    }

    public List<Film> getAllFilmsStorage() {
        List<Film> filmsList = new ArrayList<>();
        for (Integer key : films.keySet())
            filmsList.add(films.get(key));
        log.info("INFO: Все фильмы получены.");
        return filmsList;
    }

    public Film updateFilmStorage(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("INFO: Film is update.");
        } else {
            log.error("ERROR: Film с введенным id не найден.");
            throw new ValidationException("Film с введенным id не найден.");
        }
        return film;
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