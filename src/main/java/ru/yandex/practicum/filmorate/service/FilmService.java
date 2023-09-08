package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.feed.EventOperation;
import ru.yandex.practicum.filmorate.models.feed.EventType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Min Danil 13.07.2023
 */

@Slf4j
@Service
public class FilmService {

    private static int increment = 0;
    private final FilmStorage filmStorage;
    private final EventService eventService;

    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage filmStorage, EventService eventService) {
        this.filmStorage = filmStorage;
        this.eventService = eventService;
    }

    public Film addFilmService(Film film) {
        checkValidationFilm(film);
        validate(film);
        return filmStorage.addFilmStorage(film);
    }

    public List<Film> getAllFilmsService() {
        return filmStorage.getAllFilmsStorage();
    }

    public Film updateFilmService(Film film) {
        checkValidationFilm(film);
        validate(film);
        return filmStorage.updateFilmStorage(film);
    }

    public void deleteFilmService(int idFilm) {
        filmStorage.deleteFilm(idFilm);
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

    public Film getFilmById(int id) {
        return filmStorage.getFilmByIdStorage(id);
    }

    public void addUserLikeToFilmService(int id, int userId) {
        if (id < 0 || userId < 0)
            throw new NotFoundException("Неверный формат id");
        filmStorage.addLike(id, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.ADD, id);
    }

    public void deleteUserLikeFromFilmService(int id, int userId) {
        if (id < 0 || userId < 0)
            throw new NotFoundException("Неверный формат id");
        filmStorage.deleteLike(id, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.REMOVE, id);
    }

    public List<Film> getPopularFilmsService(int count, long genreId, int year) {
        List<Film> filmByYear = filmStorage.getAllFilmsStorage()
                .stream()
                .filter(film -> film.getReleaseDate().getYear() == year)
                .limit(count)
                .collect(Collectors.toList());
        List<Film> filmByGenreId = filmStorage.getAllFilmsStorage()
                .stream()
                .filter(film -> film.getGenres()
                        .stream()
                        .anyMatch(genre -> genre.getId() == genreId))
                .collect(Collectors.toList());
        if (year != 0 && genreId == 0) {
            return filmByYear.stream()
                    .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else if (year == 0 && genreId != 0) {
            return filmByGenreId.stream()
                    .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else if (year == 0) {
            return filmStorage.getAllFilmsStorage()
                .stream()
                .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        } else {
            filmByGenreId = filmByYear.stream()
                    .filter(film -> film.getGenres()
                            .stream()
                            .anyMatch(genre -> genre.getId() == genreId))
                    .collect(Collectors.toList());

         return filmByGenreId.stream()
                 .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                 .limit(10)
                 .collect(Collectors.toList());
        }
    }

    public List<Film> searchFilms(String query, String by) {
        return filmStorage.searchFilms(query, by);
    }
    private static boolean isBeforeDate(LocalDate realiseDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse("1895-12-25", formatter);
        return realiseDate.isBefore(date);
    }

    private void validate(Film film) {
        if (film.getId() == 0) {
            film.setId(getNextId());
        }
    }

    public List<Film> getFilmsByDirectorIdSortedByYearOrLikes(int directorId, String sortBy) {
        return filmStorage.getFilmsByDirectorIdSortedByYearOrLikes(directorId, sortBy);
    }

    private static int getNextId() {
        return ++increment;
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}
