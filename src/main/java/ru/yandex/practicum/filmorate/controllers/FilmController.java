package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

/**
 * @author Min Danil 06.07.2023
 */

@RestController
@RequestMapping("/films")
public class FilmController {

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return FilmService.addFilmService(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return FilmService.getAllFilmsService();
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return FilmService.updateFilmService(film);
    }
}
