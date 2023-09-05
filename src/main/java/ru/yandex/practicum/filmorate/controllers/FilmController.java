package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Min Danil 06.07.2023
 */

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilmService(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilmsService();
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilmService(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addUserLikeFilm(@Valid @PathVariable int id, @Valid @PathVariable int userId) {
        filmService.addUserLikeToFilmService(id, userId);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteUserLikeFilm(@Valid @PathVariable int id, @Valid @PathVariable int userId) {
        filmService.deleteUserLikeFromFilmService(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.getPopularFilmsService(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirectorIdSortedByYearOrLikes(@PathVariable int directorId,
                                                              @RequestParam String sortBy) {
        return filmService.getFilmsByDirectorIdSortedByYearOrLikes(directorId, sortBy);
    }
}
