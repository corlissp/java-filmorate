package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Min Danil 12.07.2023
 */

@Slf4j
@Service
public class FilmService {
    private static final Map<Integer, Film> films = new HashMap<>();

    public static Film addFilmService(Film film) {
        checkValidationFilm(film);
        int id = IdGenerator.getFreeId();
        film.setId(id);
        films.put(id, film);
        log.info("INFO: Film is saved.");
        return film;
    }

    public static List<Film> getAllFilmsService() {
        List<Film> filmsList = new ArrayList<>();
        for (Integer key : films.keySet())
            filmsList.add(films.get(key));
        log.info("INFO: Все фильмы получены.");
        return filmsList;
    }

    public static Film updateFilmService(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("INFO: Film is update.");
        } else {
            log.error("ERROR: Film с введенным id не найден.");
            throw new ValidationException("Film с введенным id не найден.");
        }
        return film;
    }

    private static void checkValidationFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("ERROR: Неверный формат name.");
            throw new ValidationException("Неверный формат name.");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("ERROR: Описание отсутствует.");
            throw new ValidationException("Описание отсутствует.");
        }
        if (film.getDescription().length() > 200) {
            log.error("ERROR: Описание длиннее 200 символов.");
            throw new ValidationException("Описание длиннее 200 символов.");
        }
        if (film.getDuration() < 0) {
            log.error("ERROR: Продолжительность фильма должна быть больше 0.");
            throw new ValidationException("Продолжительность фильма должна быть больше 0.");
        }
        if (film.getReleaseDate() == null) {
            log.error("ERROR: Дата релиза отсутствует.");
            throw new ValidationException("Дата релиза отсутствует.");
        }
        if (isBeforeDate(film.getReleaseDate())) {
            log.error("ERROR: Дата релиза раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года.");
        }
    }

    private static boolean isBeforeDate(LocalDate realiseDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("1895-12-25", formatter);
        return realiseDate.isBefore(date);
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
