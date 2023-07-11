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
        if (film.getName().isEmpty()) {
            log.error("ERROR: Неверный формат name.");
            throw new ValidationException("Неверный формат name.");
        }
        if (film.getDescription().length() > 200) {
            log.error("ERROR: Описание длиннее 200 символов.");
            throw new ValidationException("Описание длиннее 200 символов.");
        }
        if (film.getDuration() < 0) {
            log.error("ERROR: Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        if (isBeforeDate(film.getReleaseDate())) {
            log.error("ERROR: Дата релиза раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года.");
        }
        int id = IdGenerator.getFreeId();
        film.setId(id);
        films.put(id, film);
        log.info("INFO: Film is saved.");
        return film;
    }

    private boolean isBeforeDate(LocalDate realiseDate) {
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
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("INFO: Film is update.");
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
