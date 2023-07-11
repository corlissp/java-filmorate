package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Min Danil 06.07.2023
 */

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        checkValidationUser(film);
        int id = IdGenerator.getFreeId();
        film.setId(id);
        films.put(id, film);
        log.info("INFO: Film is saved.");
        return film;
    }

    private static void checkValidationUser(Film film) throws ValidationException {
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

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>();
        for (Map.Entry entry : films.entrySet()) {
            filmsList.add((Film) entry.getValue());
        }
        return filmsList;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
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
