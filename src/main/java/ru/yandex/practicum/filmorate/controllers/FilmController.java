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

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilmService(id);
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
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") int count,
                                      @RequestParam(required = false, defaultValue = "0") Long genreId,
                                      @RequestParam(required = false, defaultValue = "0") Integer year) {
        return filmService.getPopularFilmsService(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirectorIdSortedByYearOrLikes(@PathVariable int directorId,
                                                              @RequestParam String sortBy) {
        return filmService.getFilmsByDirectorIdSortedByYearOrLikes(directorId, sortBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@Valid @RequestParam("userId") Integer userId,
                                     @Valid @RequestParam("friendId") Integer friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam("query") String query,
                                  @RequestParam("by") String by) {
        return filmService.searchFilms(query, by);
    }
}
