package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Min Danil 13.07.2023
 */

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilmService(Film film) {
        return filmStorage.addFilmStorage(film);
    }

    public List<Film> getAllFilmsService() {
        return filmStorage.getAllFilmsStorage();
    }

    public Film updateFilmService(Film film) {
        return filmStorage.updateFilmStorage(film);
    }

    public static void checkValidationFilm(Film film) {
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
}
